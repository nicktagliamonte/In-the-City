package com.nicktagliamonte.characters;

import java.util.ArrayList;
import java.util.List;

import com.nicktagliamonte.items.Item;

public class Adversary extends NPC{
    private double damage;
    private double dex;
    private double ac;

    public Adversary(String name, double health, double energy, List<Item> inventory, String description, 
                     double maxHealth, double damage, double dex, double ac) {
        //adversary dialogue will only be used in specific cases -- largely, and until later in game, this will be ignored.
        super(name, health, energy, inventory, description, maxHealth, new ArrayList<String>());
        this.damage = damage;
        this.dex = dex;
        this.ac = ac;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getDex() {
        return dex;
    }

    public void setAc(double ac) {
        this.ac = ac;
    }

    public double getAc() {
        return ac;
    }

    public void setDex(double dex) {
        this.dex = dex;
    }

    public void attack(NPC target) {
        //TODO: this will be part of the combat system
        //because of the nature of checking to see whether an attack lands or what that situation will be like, it is unclear whether this method will live here
    }

    public void takeDamage(double amount) {
        //TODO: see above
        this.setHealth(this.getHealth() - amount);
    }

    public boolean isDefeated() {
        return this.getHealth() <= 0;
        //TODO: remove adversary from room -- this will be handled thru the combat ending sequence from gameState
    }
}
