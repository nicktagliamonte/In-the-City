package com.nicktagliamonte.characters;

import java.util.List;

import com.nicktagliamonte.items.Item;

public class Adversary extends NPC{
    private double attackSpeed;
    private double damage;

    public Adversary(String name, double health, double energy, List<Item> inventory, String description, 
                     double maxHealth, List<String> dialogue, double attackSpeed, double damage) {
        //adversary dialogue will only be used in specific cases -- largely, and until later in game, this will be ignored.
        super(name, health, energy, inventory, description, maxHealth, dialogue);
        this.attackSpeed = attackSpeed;
        this.damage = damage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
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

    public String talk() {
        return super.getRandomDialogue();
    }
}
