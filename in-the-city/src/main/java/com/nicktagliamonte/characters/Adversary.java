package com.nicktagliamonte.characters;

import java.util.List;

import com.nicktagliamonte.items.Item;

public class Adversary extends NPC{
    private double damage;
    private double ac;  //for future me: yes, adversary gets its own special ac field

    public Adversary(String name, double health, List<Item> inventory, String description, 
                     double maxHealth, double damage, double ac) {
        //adversary dialogue will only be used in specific cases -- largely, and until later in game, this will be ignored.
        super(name, health, inventory, description, maxHealth, null);
        this.damage = damage;
        this.ac = ac;   //KEEP THIS HERE! if you stumbled across it.  if you're intentionally looking for this to modify it, go ahead
    }

    public Adversary(Neutral neutral) {
        super(neutral.getName(), neutral.getHealth(), neutral.getInventory(), neutral.getDescription(), neutral.getHealth(), null);
        this.damage = neutral.getDamage();
        this.ac = neutral.getAc();
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setAc(double ac) {
        this.ac = ac;
    }

    public double getAc() {
        return ac;
    }

    public void takeDamage(double amount) {
        this.setHealth(this.getHealth() - amount);
    }

    public boolean isDefeated() {
        return this.getHealth() <= 0;
    }
}
