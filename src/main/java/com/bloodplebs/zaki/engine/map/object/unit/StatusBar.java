package com.bloodplebs.zaki.engine.map.object.unit;

import java.util.logging.Level;

public class StatusBar {

    private int attackPower;
    private int magicPower;
    private int armor;
    private int magicResist;
    private int health;
    private int mana;
    private int experience;

    public StatusBar(int attackPower, int magicPower, int armor, int magicResist, int health, int mana) {
        this.attackPower = attackPower;
        this.magicPower = magicPower;
        this.armor = armor;
        this.magicResist = magicResist;
        this.health = health;
        this.mana = mana;
        this.experience = 0;
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

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void setMagicPower(int magicPower) {
        this.magicPower = magicPower;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setMagicResist(int magicResist) {
        this.magicResist = magicResist;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
