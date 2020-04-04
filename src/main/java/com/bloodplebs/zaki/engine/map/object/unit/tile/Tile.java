package com.bloodplebs.zaki.engine.map.object.unit.tile;

import java.io.Serializable;

public interface Tile extends Serializable {
    enum TileType {
        WALL, ITEM, PLAYER, NPC, PATH, PLAYER_ATTACK
    }

    String print();

    TileType getType();
}
