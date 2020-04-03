package com.bloodplebs.zaki.engine.map.object.unit.tile.impl;

import com.bloodplebs.zaki.engine.map.object.unit.tile.Tile;

public class Item extends TileImpl {
    public Item() {
        super(TileType.ITEM);
    }

    @Override
    public String print() {
        return "I";
    }
}
