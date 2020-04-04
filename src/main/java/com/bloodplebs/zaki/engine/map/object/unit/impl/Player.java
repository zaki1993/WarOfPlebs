package com.bloodplebs.zaki.engine.map.object.unit.impl;

import com.bloodplebs.zaki.engine.map.geometry.Point;
import com.bloodplebs.zaki.engine.map.object.unit.StatusBar;
import com.bloodplebs.zaki.engine.map.object.unit.Unit;
import com.bloodplebs.zaki.engine.map.object.unit.core.Direction;
import com.bloodplebs.zaki.engine.map.object.unit.race.Race;
import com.bloodplebs.zaki.engine.map.object.unit.tile.impl.Item;

import java.util.Objects;

public class Player extends Unit {

    private Point playerLocation;

    private Direction lastPlayerMove;

    private int itemsCollected;

    private String username;

    public Player(String username, Race race) {
        super(getPlayerStatusBar(), race, TileType.UNIT);
        this.username = username;
        this.itemsCollected = 0;

        // By default attack on right side
        this.lastPlayerMove = Direction.RIGHT;
    }

    @Override
    public void collectItem(Item i) {
        if (i != null) {
            itemsCollected++;
            // TODO
        }
    }

    @Override
    public void move(Direction direction, Point location) {
        this.lastPlayerMove = direction;
        this.playerLocation = location;
    }

    public Point getPlayerLocation() {
        return playerLocation;
    }

    public Direction getLastPlayerMove() {
        return lastPlayerMove;
    }

    public int getItemsCollected() {
        return itemsCollected;
    }

    private static StatusBar getPlayerStatusBar() {
        return new StatusBar(30, 10, 5, 5, 100, 60);
    }

    @Override
    public String print() {
        return "P";
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(username, player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public void setLastPositionMove(Direction direction) {
        this.lastPlayerMove = direction;
    }

    public void spawn(Point point) {
        this.playerLocation = point;
    }
}
