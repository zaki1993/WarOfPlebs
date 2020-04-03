package com.bloodplebs.zaki.engine.map;

import com.bloodplebs.zaki.engine.map.geometry.Point;
import com.bloodplebs.zaki.engine.map.object.unit.core.Direction;
import com.bloodplebs.zaki.engine.map.object.unit.impl.NPC;
import com.bloodplebs.zaki.engine.map.object.unit.tile.impl.Path;
import com.bloodplebs.zaki.engine.map.object.unit.tile.impl.Wall;
import com.bloodplebs.zaki.engine.map.object.unit.tile.impl.Item;
import com.bloodplebs.zaki.engine.map.object.unit.tile.Tile;
import com.bloodplebs.zaki.engine.map.object.unit.impl.Player;

import java.io.Serializable;

public class BloorPlebsMap implements Serializable {

    private static final int DEFAULT_MAP_SIZE = 1000;

    private static final int MAX_ITEMS_ON_MAP_PERCENT = 2;

    private static final int MAX_NPC_ON_MAP_PERCENT = 10;

    private static final int SPAWN_ITEM_PERCENTAGE = 1;

    private static final int SPAWN_NPC_PERCENTAGE = 2;

    private static final char WALL = '#';

    private final Tile[][] map;

    private int currentItemsOnMap;

    private int currentNpcOnMap;

    public BloorPlebsMap() {
        this(DEFAULT_MAP_SIZE);
    }

    public BloorPlebsMap(int mapSize) {
        this.map = new Tile[mapSize][mapSize];
        this.currentNpcOnMap = 0;
        this.currentItemsOnMap = 0;
        generateMap();
    }

    private void generateMap() {
        generateWalls();
        fillPaths();
    }

    private void fillPaths() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == null) {
                    map[i][j] = new Path();
                }
            }
        }
    }

    /**
     * Generates random items on the map. The number of the items is equal to 2% of the map size.
     */
    public void spawnItems() {
        int maxItems = (map.length * map.length * MAX_ITEMS_ON_MAP_PERCENT) / 100;
        int itemsToSpawn = (map.length * map.length * SPAWN_ITEM_PERCENTAGE) / 100;

        if (currentItemsOnMap + itemsToSpawn >= maxItems) {
            itemsToSpawn = maxItems - currentItemsOnMap;
        }

        do {
            // generate random position for the item
            int x = (int) (Math.random() * (map.length - 2) + 1);
            int y = (int) (Math.random() * (map.length - 2) + 1);

            if (map[x][y] != null && map[x][y].getType() == Tile.TileType.PATH) {
                Item item = getRandomItem();
                map[x][y] = item;
                itemsToSpawn--;
                currentItemsOnMap++;
            }
        } while (itemsToSpawn > 0);
    }

    private Item getRandomItem() {
        return new Item();
    }

    private void generateWalls() {
        // Top and bottom wall
        for (int i = 0; i < map.length; i++) {
            map[0][i] = new Wall();
            map[map.length - 1][i] = new Wall();
        }

        // Left and right wall
        for (int i = 1; i < map.length - 1; i++) {
            map[i][0] = new Wall();
            map[i][map.length - 1] = new Wall();
        }
    }

    public void print() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] != null) {
                    System.out.print(map[i][j].print() + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    public String getAsString() {

        StringBuilder mapAsString = new StringBuilder();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] != null) {
                    mapAsString.append(map[i][j].print()).append(" ");
                } else {
                    mapAsString.append("  ");
                }
            }
            mapAsString.append("\n");
        }

        return mapAsString.toString();
    }

    public void spawnPlayer(Player player) {

        boolean playerSpawned = false;
        do {
            // generate random position for the item
            int x = (int) (Math.random() * (map.length - 2) + 1);
            int y = (int) (Math.random() * (map.length - 2) + 1);

            if (map[x][y].getType() == Tile.TileType.PATH) {
                map[x][y] = player;
                playerSpawned = true;
            }
        } while (!playerSpawned);
    }

    public void removePlayer(Player player) {
        Point playerLocation = findPlayer(player);
        map[playerLocation.getX()][playerLocation.getY()] = new Path();
    }

    public void spawnNpc() {
        int maxNpc = (map.length * map.length * MAX_NPC_ON_MAP_PERCENT) / 100;
        int npcToSpawn = (map.length * map.length * SPAWN_NPC_PERCENTAGE) / 100;

        if (currentNpcOnMap + npcToSpawn >= maxNpc) {
            npcToSpawn = maxNpc - currentNpcOnMap;
        }

        do {
            // generate random position for the item
            int x = (int) (Math.random() * (map.length - 2) + 1);
            int y = (int) (Math.random() * (map.length - 2) + 1);

            if (map[x][y] != null && map[x][y].getType() == Tile.TileType.PATH) {
                NPC npc = generateRandomNpc();
                map[x][y] = npc;
                npcToSpawn--;
                currentNpcOnMap++;
            }
        } while (npcToSpawn > 0);
    }

    private NPC generateRandomNpc() {
        return new NPC();
    }

    public void movePlayer(Direction direction, Player player) {
        Point playerLocation = findPlayer(player);

        int newX = playerLocation.getX();
        int newY = playerLocation.getY();
        if (direction == Direction.LEFT) {
            newX -= 1;
        } else if (direction == Direction.RIGHT) {
            newX += 1;
        } else if (direction == Direction.UP) {
            newY -= 1;
        } else {
            newY += 1;
        }

        boolean isValidMove = isValidLocation(newX, newY);
        if (isValidMove) {
            map[newX][newY] = player;
            map[playerLocation.getY()][playerLocation.getY()] = new Path();
        }
    }

    private Point findPlayer(Player p) {

        Point playerLocation = null;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j].equals(p)) {
                    playerLocation = new Point(i, j);
                    break;
                }
            }
            if (playerLocation != null) {
                break;
            }
        }

        return playerLocation;
    }

    private boolean isValidLocation(int x, int y) {
        return x >= 1 && y >= 1 && x <= map.length - 1 && y <= map.length - 1 && map[x][y] != null && map[x][y].getType() == Tile.TileType.PATH;
    }
}
