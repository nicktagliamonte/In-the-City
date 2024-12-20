package com.nicktagliamonte.characters;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class Neutral extends NPC{
    @Expose private List<String> questDialogue;
    @Expose private boolean canGiveQuest;
    @Expose private double damage;
    @Expose private double moralityFlag;

    public Neutral(String name, double health, List<Item> inventory, String description, double maxHealth,
                  List<String> questDialogue, boolean canGiveQuest, double damage, double moralityFlag, double ac, double str, double dex, double con, 
                  double Intelligence, double wis, double charisma, double alignmentImpact, boolean dynamicDialogue) {
        super(name, health, inventory, description, maxHealth, str, dex, con, Intelligence, wis, charisma, ac, alignmentImpact, dynamicDialogue);
        this.questDialogue = questDialogue;
        this.canGiveQuest = canGiveQuest;
        this.damage = damage;
        this.moralityFlag = moralityFlag;
    }

    public Adversary actAsAdversary() {
        return new Adversary(this);
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public boolean isDefeated() {
        return this.getHealth() <= 0;
    }

    public double getMoralityFlag() {
        return moralityFlag;
    }
}
