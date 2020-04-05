package com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.attack;

import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.TileImpl;
import com.warofplebs.zaki.server.engine.map.object.unit.tile.Tile;

public abstract class PlayerAttack extends TileImpl {
    public PlayerAttack() {
        super(Tile.TileType.PLAYER_ATTACK);
    }
}
