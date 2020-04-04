package com.bloodplebs.zaki.engine.map.object.unit.tile.impl.attack;

import com.bloodplebs.zaki.engine.map.object.unit.tile.impl.TileImpl;

public class PlayerAttack extends TileImpl {
    public PlayerAttack() {
        super(TileType.PLAYER_ATTACK);
    }

    @Override
    public String print() {
        return "A";
    }
}
