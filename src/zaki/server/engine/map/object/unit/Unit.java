package com.warofplebs.zaki.server.engine.map.object.unit;

import com.warofplebs.zaki.server.engine.map.geometry.Point;
import com.warofplebs.zaki.server.engine.map.object.unit.core.Direction;
import com.warofplebs.zaki.server.engine.map.object.unit.race.Race;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.item.Item;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.TileImpl;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.Tile;

/**
 * Structure which will define a unit in the game. The units are the living things in the game.
 * Each unit has attack power, magic power, armor, magic resist, health and mana. Each unit has a race.
 * And each race has its own unique ability which the unit can cast.
 */
public abstract class Unit extends TileImpl {

    private enum Level {
        NEW;
    }

    private Point location;

    private boolean isUnitUnderAttack;

    private Direction directionOfAttack;

    private Direction lastUnitMove;

    private StatusBar statusBar;
    private Level level;
    private Race race;

    public Unit(StatusBar statusBar, Race race, Tile.TileType type) {
        super(type);
        this.statusBar = statusBar;
        this.level = Level.NEW;
        this.race = race;

        // By default attack on right side
        this.lastUnitMove = Direction.RIGHT;
    }

    protected StatusBar getStatusBar() {
        return statusBar;
    }

    public void setLastPositionMove(Direction direction) {
        this.lastUnitMove = direction;
    }

    public Direction getLastPlayerMove() {
        return lastUnitMove;
    }

    public Level getLevel() {
        return level;
    }

    public Direction getDirectionOfAttack() {
        return directionOfAttack;
    }

    public boolean isUnitUnderAttack() {
        return isUnitUnderAttack;
    }

    public void setUnitAttacked(boolean attacked, Direction direction) {
        setUnitAttacked(attacked);
        setDirectionOfAttack(direction);
    }

    public void launchAttack(Unit u) {
        u.takePhysicalDamage(getAttackPower());
        if (u.isUnitDead()) {
            gainExperience();
        }
    }

    public void levelUp() {

    }

    public void setUnitLocation(Point location) {
        this.location = location;
    }

    public Point getUnitLocation() {
        return location;
    }

    public abstract void collectItem(Item i);

    public void move(Direction direction, Point location) {
        setLastPositionMove(direction);
        setUnitLocation(location);
    }

    private void takePhysicalDamage(int attack) {
        int dmgToReceive = attack - statusBar.getArmor();
        if (dmgToReceive < 0) {
            dmgToReceive = 0;
        }
        int currentHp = statusBar.getHealth();
        currentHp -= dmgToReceive;

        if (currentHp < 0) {
            currentHp = 0;
        }
        statusBar.setHealth(currentHp);
    }

    private void takeMagicalDamage(int magic) {
    }

    public boolean isUnitDead() {
        return statusBar.getHealth() <= 0;
    }

    protected int getAttackPower() {
        return statusBar.getAttackPower();
    }

    protected int getMagicPower() {
        return statusBar.getMagicPower();
    }

    private void setDirectionOfAttack(Direction direction) {
        this.directionOfAttack = direction;
    }

    private void setUnitAttacked(boolean attacked) {
        this.isUnitUnderAttack = attacked;
    }

    protected void gainExperience() {
        statusBar.setExperience(statusBar.getExperience() + 5);
    }
}
