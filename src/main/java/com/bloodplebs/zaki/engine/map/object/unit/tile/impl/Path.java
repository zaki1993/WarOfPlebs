package com.bloodplebs.zaki.engine.map.object.unit.tile.impl;

import com.bloodplebs.zaki.engine.map.object.unit.tile.Tile;

public class Path extends TileImpl {
    public Path() {
        super(TileType.PATH);
    }

    @Override
    public String print() {
        return "_";
    }
}
