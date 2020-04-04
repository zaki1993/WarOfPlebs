package com.bloodplebs.zaki.engine.map.object.unit.impl;

import com.bloodplebs.zaki.engine.map.geometry.Point;
import com.bloodplebs.zaki.engine.map.object.unit.StatusBar;
import com.bloodplebs.zaki.engine.map.object.unit.Unit;
import com.bloodplebs.zaki.engine.map.object.unit.core.Direction;
import com.bloodplebs.zaki.engine.map.object.unit.race.Race;
import com.bloodplebs.zaki.engine.map.object.unit.tile.impl.Item;

public class NPC extends Unit {

    private static final String NPC_ATTACKED_VERTICAL = "NPC_VERTICAL_ATTACK";

    private static final String NPC_ATTACKED_HORIZONTAL = "NPC_HORIZONTAL_ATTACK";

    private static final String NPC_NOT_ATTACKED = "NPC";

    public NPC() {
        super(getNpcStatusBar(), Race.NPC, TileType.UNIT);
    }

    @Override
    public String print() {
        Direction directionOfAttack = getDirectionOfAttack();
        return isUnitUnderAttack() && directionOfAttack != null ? (directionOfAttack == Direction.UP || directionOfAttack == Direction.DOWN)
                                                             ? NPC_ATTACKED_VERTICAL : NPC_ATTACKED_HORIZONTAL
                                                             : NPC_NOT_ATTACKED;
    }

    @Override
    public void launchAttack(Unit u) {

    }

    @Override
    public void levelUp() {

    }

    @Override
    public void collectItem(Item i) {

    }

    @Override
    public void move(Direction direction, Point location) {

    }

    private static StatusBar getNpcStatusBar() {
        return new StatusBar(10, 10, 5, 5, 100, 60);
    }
}
