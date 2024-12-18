package com.nicktagliamonte.characters;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class Adversary extends NPC{
    @Expose private double damage;
    @Expose private List<String> allies = new ArrayList<>();

    public Adversary(String name, double health, List<Item> inventory, String description, double maxHealth, double damage, double ac, double str, double dex, 
                     double con, double Intelligence, double wis, double charisma, double alignmentImpact, List<String> allies, boolean dynamicDialogue) {
        //adversary dialogue will only be used in specific cases -- largely, and until later in game, this will be ignored.
        super(name, health, inventory, description, maxHealth, str, dex, con, Intelligence, wis, charisma, ac, alignmentImpact, dynamicDialogue);
        this.damage = damage;
        this.allies = allies;
    }

    public Adversary(Neutral neutral) {
        super(neutral.getName(), neutral.getHealth(), neutral.getInventory(), neutral.getDescription(), neutral.getHealth(), neutral.getStrength(),
        neutral.getDexterity(), neutral.getConstitution(), neutral.getIntelligence(), neutral.getWisdom(), neutral.getCharisma(), neutral.getAc(), 
        neutral.getAlignmentImpact(), neutral.getDynamicDialogue());
        this.damage = neutral.getDamage();
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
        this.setHealth(this.getHealth() - amount);
    }

    public boolean isDefeated() {
        return this.getHealth() <= 0;
    }
}
