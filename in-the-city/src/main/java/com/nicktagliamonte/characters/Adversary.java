package com.nicktagliamonte.characters;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class Adversary extends NPC {
    @Expose private double damage;
    @Expose private List<String> allies;

    // Primary constructor
    public Adversary(String name, double health, List<Item> inventory, String description, double maxHealth, double damage, double ac, double str, 
                     double dex, double con, double intelligence, double wis, double charisma, double alignmentImpact, List<String> allies, boolean dynamicDialogue) {
        super(name, health, inventory, description, maxHealth, str, dex, con, intelligence, wis, charisma, ac, alignmentImpact, dynamicDialogue);
        this.damage = damage;
        this.allies = allies;
    }

    // Copy constructor for converting from Neutral
    public Adversary(Neutral neutral) {
        this(neutral.getName(), neutral.getHealth(), neutral.getInventory(), neutral.getDescription(), neutral.getHealth(), neutral.getDamage(), 
             neutral.getAc(), neutral.getStrength(), neutral.getDexterity(), neutral.getConstitution(), neutral.getIntelligence(), neutral.getWisdom(), 
             neutral.getCharisma(), neutral.getAlignmentImpact(), new ArrayList<>(), neutral.getDynamicDialogue());
    }

    public List<String> getAllies() {
        return allies;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void takeDamage(double amount) {
        // Reduce health by damage amount
        this.setHealth(this.getHealth() - amount);
    }

    public boolean isDefeated() {
        // Return true if health is less than or equal to zero
        return this.getHealth() <= 0;
    }
}