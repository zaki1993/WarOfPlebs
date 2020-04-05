package com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.item.impl;

import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.item.Item;

public class RangeIncreaseItem extends Item {

    public RangeIncreaseItem() {
        super(ItemType.RANGE);
    }

    @Override
    public int getBonus() {
        return 1;
    }
}
