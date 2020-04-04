package com.bloodplebs.zaki.engine.map.object.unit.tile.impl.attack;

import com.bloodplebs.zaki.engine.map.object.unit.tile.impl.TileImpl;

public abstract class PlayerAttack extends TileImpl {
    public PlayerAttack() {
        super(TileType.PLAYER_ATTACK);
    }
}
