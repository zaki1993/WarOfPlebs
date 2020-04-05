package com.warofplebs.zaki.server.engine.map.object.unit.tile;

import java.io.Serializable;

public interface Tile extends Serializable {
    enum TileType {
        WALL, ITEM, UNIT, PATH, PLAYER_ATTACK
    }

    String print();

    TileType getType();
}
