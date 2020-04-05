package com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.item;

import com.warofplebs.zaki.server.engine.map.object.unit.tile.impl.TileImpl;

public abstract class Item extends TileImpl {

    public enum ItemType {
        RANGE, ATTACK, HEALTH, MANA, DEFENSE, NONE
    }

    private ItemType type;

    public Item(ItemType type) {
        super(TileType.ITEM);
        this.type = type;
    }

    @Override
    public String print() {
        return "I";
    }

    public ItemType getItemType() {
        return type;
    }

    public abstract int getBonus();
}
