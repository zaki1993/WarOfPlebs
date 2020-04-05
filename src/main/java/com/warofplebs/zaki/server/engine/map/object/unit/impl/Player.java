package com.warofplebs.zaki.server.engine.map.object.unit.impl;

import com.warofplebs.zaki.server.engine.map.geometry.Point;
import com.warofplebs.zaki.server.engine.map.object.unit.StatusBar;
import com.warofplebs.zaki.server.engine.map.object.unit.Unit;
import com.warofplebs.zaki.server.engine.map.object.unit.race.Race;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.item.Item;

import java.util.Objects;

public class Player extends Unit {

    private String username;

    public Player(String username, Race race) {
        super(getPlayerStatusBar(), race, TileType.UNIT);
        this.username = username;
    }

    @Override
    public void collectItem(Item i) {
        if (i != null) {
            Item.ItemType type = i.getItemType();
            if (type == Item.ItemType.RANGE) {
                getStatusBar().setRange(getStatusBar().getRange() + i.getBonus());
            }
        }
    }

    private static StatusBar getPlayerStatusBar() {
        return new StatusBar(30, 10, 5, 5, 100, 60, 1);
    }

    public String getUsername() {
        return username;
    }

    public void spawn(Point point) {
        setUnitLocation(point);
    }

    public int getPlayerRange() {
        return getStatusBar().getRange();
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

    @Override
    public String print() {
        return "P";
    }
}
