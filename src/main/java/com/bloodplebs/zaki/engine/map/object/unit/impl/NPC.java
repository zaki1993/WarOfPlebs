package com.bloodplebs.zaki.engine.map.object.unit.impl;

import com.bloodplebs.zaki.engine.map.object.unit.tile.impl.TileImpl;

public class NPC extends TileImpl {

    public NPC() {
        super(TileType.NPC);
    }

    @Override
    public String print() {
        return "N";
    }
}
