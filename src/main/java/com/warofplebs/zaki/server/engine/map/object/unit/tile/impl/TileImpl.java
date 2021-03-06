package com.warofplebs.zaki.server.engine.map.object.unit.tile.impl;

import com.warofplebs.zaki.server.engine.map.object.unit.tile.Tile;

public abstract class TileImpl implements Tile {

    private TileType type;

    public TileImpl(TileType type) {
        this.type = type;
    }

    @Override
    public TileType getType() {
        return type;
    }
}
