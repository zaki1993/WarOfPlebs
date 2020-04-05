package com.warofplebs.zaki.server.engine.map.object.unit.tile.impl;

public class Wall extends TileImpl {

    public Wall() {
        super(TileType.WALL);
    }

    @Override
    public String print() {
        return "#";
    }
}
