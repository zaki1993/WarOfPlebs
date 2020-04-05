package com.warofplebs.zaki.server.engine.map;

import com.warofplebs.zaki.server.engine.map.geometry.Point;
import com.warofplebs.zaki.server.engine.map.object.unit.Unit;
import com.warofplebs.zaki.server.engine.map.object.unit.core.Direction;
import com.warofplebs.zaki.server.engine.map.object.unit.impl.NPC;
import com.warofplebs.zaki.server.engine.map.object.unit.impl.Player;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.Tile;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.Path;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.Wall;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.attack.impl.HorizontalPlayerAttack;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.attack.impl.VerticalPlayerAttack;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.item.Item;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.item.impl.RangeIncreaseItem;

import java.io.Serializable;

public class BloorPlebsMap implements Serializable {

    private static final int DEFAULT_MAP_SIZE = 1000;

    private static final int MAX_ITEMS_ON_MAP_PERCENT = 2;

    private static final int MAX_NPC_ON_MAP_PERCENT = 10;

    private static final int SPAWN_ITEM_PERCENTAGE = 1;

    private static final int SPAWN_NPC_PERCENTAGE = 2;

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

    public String[][] getAsString() {

        String[][] result = new String[map.length][map.length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                result[i][j] = map[i][j].print();
            }
        }

        return result;
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
                player.spawn(new Point(x, y));
            }
        } while (!playerSpawned);
    }

    public void removePlayer(Player player) {
        Point playerLocation = player.getUnitLocation();
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
                Unit npc = generateRandomNpc();
                npc.setUnitLocation(new Point(x, y));
                map[x][y] = npc;
                npcToSpawn--;
                currentNpcOnMap++;
            }
        } while (npcToSpawn > 0);
    }

    private Unit generateRandomNpc() {
        return new NPC();
    }

    public void launchAttack(Player player) {

        Point playerLocation = player.getUnitLocation();
        int oldX = playerLocation.getX();
        int oldY = playerLocation.getY();

        Direction direction = player.getLastPlayerMove();

        int playerRange = player.getPlayerRange();
        if (direction == Direction.LEFT) {
            launchHorizontalLeftAttack(new Point(oldX, oldY), new Point(oldX - playerRange, oldY), player, direction);
        } else if (direction == Direction.RIGHT) {
            launchHorizontalRightAttack(new Point(oldX, oldY), new Point(oldX + playerRange, oldY), player, direction);
        } else if (direction == Direction.UP) {
            launchVerticalUpAttack(new Point(oldX, oldY), new Point(oldX, oldY - playerRange), player, direction);
        } else {
            launchVerticalDownAttack(new Point(oldX, oldY), new Point(oldX, oldY + playerRange), player, direction);
        }
    }

    private void launchVerticalDownAttack(Point from, Point to, Player player, Direction direction) {

        int oldX = from.getX();
        int oldY = from.getY();
        int newY = to.getY();

        // X is changing, Y stays the same when we have vertical attack. Y is the column, X is the row
        for (int y = oldY + 1; y <= newY; y++) {
            boolean continueToNextPosition = true;
            if (isValidPositionForAttack(oldX, y)) {
                if (map[oldX][y].getType() == Tile.TileType.PATH) {
                    map[oldX][y] = new VerticalPlayerAttack();
                } else {
                    Unit unit = (Unit) map[oldX][y];
                    unit.setUnitAttacked(true, direction);
                    player.launchAttack(unit);
                    continueToNextPosition = false;
                }
            } else {
                continueToNextPosition = false;
            }

            // If we hit any tile different than PATH, then stop there and do not draw any further
            if (!continueToNextPosition) {
                break;
            }
        }
    }

    private void launchVerticalUpAttack(Point from, Point to, Player player, Direction direction) {

        int oldX = from.getX();
        int oldY = from.getY();
        int newY = to.getY();

        // X is changing, Y stays the same when we have vertical attack. Y is the column, X is the row
        for (int y = oldY - 1; y >= newY; y--) {
            boolean continueToNextPosition = true;
            if (isValidPositionForAttack(oldX, y)) {
                if (map[oldX][y].getType() == Tile.TileType.PATH) {
                    map[oldX][y] = new VerticalPlayerAttack();
                } else {
                    Unit unit = (Unit) map[oldX][y];
                    unit.setUnitAttacked(true, direction);
                    player.launchAttack(unit);
                    continueToNextPosition = false;
                }
            } else {
                continueToNextPosition = false;
            }

            // If we hit any tile different than PATH, then stop there and do not draw any further
            if (!continueToNextPosition) {
                break;
            }
        }
    }

    private void launchHorizontalRightAttack(Point from, Point to, Player player, Direction direction) {

        int oldX = from.getX();
        int oldY = from.getY();
        int newX = to.getX();

        // X is changing, Y stays the same when we have vertical attack. Y is the column, X is the row
        for (int x = oldX + 1; x <= newX; x++) {
            boolean continueToNextPosition = true;
            if (isValidPositionForAttack(x, oldY)) {
                if (map[x][oldY].getType() == Tile.TileType.PATH) {
                    map[x][oldY] = new HorizontalPlayerAttack();
                } else {
                    Unit unit = (Unit) map[x][oldY];
                    unit.setUnitAttacked(true, direction);
                    player.launchAttack(unit);
                    continueToNextPosition = false;
                }
            } else {
                continueToNextPosition = false;
            }

            // If we hit any tile different than PATH, then stop there and do not draw any further
            if (!continueToNextPosition) {
                break;
            }
        }
    }

    private void launchHorizontalLeftAttack(Point from, Point to, Player player, Direction direction) {

        int oldX = from.getX();
        int oldY = from.getY();
        int newX = to.getX();

        // X is changing, Y stays the same when we have vertical attack. Y is the column, X is the row
        for (int x = oldX - 1; x >= newX; x--) {
            boolean continueToNextPosition = true;
            if (isValidPositionForAttack(x, oldY)) {
                if (map[x][oldY].getType() == Tile.TileType.PATH) {
                    map[x][oldY] = new HorizontalPlayerAttack();
                } else {
                    Unit unit = (Unit) map[x][oldY];
                    unit.setUnitAttacked(true, direction);
                    player.launchAttack(unit);
                    continueToNextPosition = false;
                }
            } else {
                continueToNextPosition = false;
            }

            // If we hit any tile different than PATH, then stop there and do not draw any further
            if (!continueToNextPosition) {
                break;
            }
        }
    }

    public void moveUnit(Direction direction, Unit unit) {
        Point unitLocation = unit.getUnitLocation();

        int newX = unitLocation.getX();
        int newY = unitLocation.getY();

        int oldX = newX;
        int oldY = newY;
        if (direction == Direction.LEFT) {
            newX -= 1;
        } else if (direction == Direction.RIGHT) {
            newX += 1;
        } else if (direction == Direction.UP) {
            newY -= 1;
        } else {
            newY += 1;
        }

        if (isValidPositionForMove(newX, newY)) {
            Tile currentObjOnMap = map[newX][newY];
            if (currentObjOnMap.getType() == Tile.TileType.ITEM) {
                collectItem(unit, (Item) currentObjOnMap);
            }
            map[newX][newY] = unit;
            map[oldX][oldY] = new Path();

            // Call unit.move(), provides additional independent logic to the unit
            unit.move(direction, new Point(newX, newY));
        } else {
            unit.setLastPositionMove(direction);
        }
    }

    public int getSize() {
        return map.length;
    }

    // Clears attacks and other junks on the map
    public void clearJunk() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j].getType() == Tile.TileType.PLAYER_ATTACK) {
                    map[i][j] = new Path();
                } else if (map[i][j].getType() == Tile.TileType.UNIT) {
                    Unit u = (Unit) map[i][j];
                    // If the unit is dead remove it, otherwise set unit as not attacked
                    if (u.isUnitDead()) {
                        map[i][j] = new Path();
                        // If the dead unit is NPC reduce the number of NPCs on the map
                        if (u instanceof NPC) {
                            currentNpcOnMap--;
                        }
                    } else {
                        u.setUnitAttacked(false, null);
                    }
                }
            }
        }
    }

    private void collectItem(Unit unit, Item currentObjOnMap) {
        unit.collectItem(currentObjOnMap);
        currentItemsOnMap--;
    }

    private boolean isValidPositionForMove(int x, int y) {
        return x >= 1 && y >= 1 && x <= map.length - 1 && y <= map.length - 1 && map[x][y] != null && (map[x][y].getType() == Tile.TileType.PATH || map[x][y].getType() == Tile.TileType.ITEM);
    }

    private boolean isValidPositionForAttack(int x, int y) {
        return x >= 1 && y >= 1 && x <= map.length - 1 && y <= map.length - 1 && map[x][y] != null && (map[x][y].getType() == Tile.TileType.PATH || map[x][y].getType() == Tile.TileType.UNIT);
    }

    private Item getRandomItem() {
        // TODO
        return new RangeIncreaseItem();
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

    public void moveNpc() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] instanceof NPC) {
                    Direction randomDirection = generateDirection();
                    // This might be null if the unit does not want to move
                    if (randomDirection != null) {
                        moveUnit(randomDirection, (Unit) map[i][j]);
                    }
                }
            }
        }
    }

    private Direction generateDirection() {
        // TODO fix it
        // prosto me murzi da go pisha koda i go ostawih taka :D
        Direction direction = null;
        try {
            direction = Direction.valueOf(new String[]{"LEFT", "RIGHT", "UP", "DOWN", "NONE"}[(int) (Math.random() * 4)]);
        } catch (IllegalArgumentException iae) {
            // DO NOTHING
        }

        return direction;
    }
}
