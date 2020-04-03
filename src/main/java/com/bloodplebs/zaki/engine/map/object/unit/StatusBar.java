package com.bloodplebs.zaki.engine.map.object.unit;

public class StatusBar {

    private int attackPower;
    private int magicPower;
    private int armor;
    private int magicResist;
    private int health;
    private int mana;

    public StatusBar(int attackPower, int magicPower, int armor, int magicResist, int health, int mana) {
        this.attackPower = attackPower;
        this.magicPower = magicPower;
        this.armor = armor;
        this.magicResist = magicResist;
        this.health = health;
        this.mana = mana;
    }

    public void update(int attackPower, int magicPower, int armor, int magicResist, int health, int mana) {
        this.attackPower += attackPower;
        this.magicPower += magicPower;
        this.armor += armor;
        this.magicResist += magicResist;
        this.health += health;
        this.mana += mana;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getMagicPower() {
        return magicPower;
    }

    public int getArmor() {
        return armor;
    }

    public int getMagicResist() {
        return magicResist;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }
}
