package com.warofplebs.zaki.server.engine.map.object.unit.tile.impl;

public class Path extends TileImpl {
    public Path() {
        super(TileType.PATH);
    }

    @Override
    public String print() {
        return "_";
    }
}
