package com.nicktagliamonte.characters;

import java.util.List;

import com.nicktagliamonte.items.Item;

public class Adversary extends NPC{
    private double damage;

    public Adversary(String name, double health, List<Item> inventory, String description, double maxHealth, double damage, double ac, double str, double dex, 
                     double con, double intelligence, double wis, double charisma, double alignmentImpact) {
        //adversary dialogue will only be used in specific cases -- largely, and until later in game, this will be ignored.
        super(name, health, inventory, description, maxHealth, str, dex, con, intelligence, wis, charisma, ac, alignmentImpact);
        this.damage = damage;
    }

    public Adversary(Neutral neutral) {
        super(neutral.getName(), neutral.getHealth(), neutral.getInventory(), neutral.getDescription(), neutral.getHealth(), neutral.getStrength(),
        neutral.getDexterity(), neutral.getConstitution(), neutral.getIntelligence(), neutral.getWisdom(), neutral.getCharisma(), neutral.getAc(), neutral.getAlignmentImpact());
        this.damage = neutral.getDamage();
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void takeDamage(double amount) {
        this.setHealth(this.getHealth() - amount);
    }

    public boolean isDefeated() {
        return this.getHealth() <= 0;
    }
}
