package com.bloodplebs.zaki.engine.map.object.unit;

import com.bloodplebs.zaki.engine.map.BloorPlebsMap;
import com.bloodplebs.zaki.engine.map.object.unit.core.Direction;
import com.bloodplebs.zaki.engine.map.object.unit.race.Race;
import com.bloodplebs.zaki.engine.map.object.unit.tile.impl.TileImpl;

/**
 * Structure which will define a unit in the game. The units are the living things in the game.
 * Each unit has attack power, magic power, armor, magic resist, health and mana. Each unit has a race.
 * And each race has its own unique ability which the unit can cast.
 */
public abstract class Unit extends TileImpl {

    private enum Level {
        NEW;
    }

    private StatusBar statusBar;
    private Level level;
    private Race race;

    public Unit(StatusBar statusBar, Race race, TileType type) {
        super(type);
        this.statusBar = statusBar;
        this.level = Level.NEW;
        this.race = race;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public Level getLevel() {
        return level;
    }

    public abstract void fight(Unit other);
    public abstract void levelUp();
}
