package com.bloodplebs.zaki.engine.map.object.unit.impl;

import com.bloodplebs.zaki.engine.map.BloorPlebsMap;
import com.bloodplebs.zaki.engine.map.object.unit.StatusBar;
import com.bloodplebs.zaki.engine.map.object.unit.Unit;
import com.bloodplebs.zaki.engine.map.object.unit.core.Direction;
import com.bloodplebs.zaki.engine.map.object.unit.race.Race;

public class Player extends Unit {

    private String username;

    public Player(String username, Race race) {
        super(getPlayerStatusBar(), race, TileType.PLAYER);
        this.username = username;
    }

    @Override
    public void fight(Unit other) {

    }

    @Override
    public void levelUp() {

    }

    private static StatusBar getPlayerStatusBar() {
        return new StatusBar(10, 10, 5, 5, 100, 60);
    }

    @Override
    public String print() {
        return "P";
    }

    public String getUsername() {
        return username;
    }
}
