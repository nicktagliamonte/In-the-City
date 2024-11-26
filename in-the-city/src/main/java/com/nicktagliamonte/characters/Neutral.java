package com.nicktagliamonte.characters;

import java.util.List;

import com.nicktagliamonte.items.Item;

public class Neutral extends NPC{
    private List<String> questDialogue;
    private boolean canGiveQuest;
    private double damage;
    private double moralityFlag;

    public Neutral(String name, double health, List<Item> inventory, String description, double maxHealth,
                  List<String> questDialogue, boolean canGiveQuest, double damage, double moralityFlag, double ac, double str, double dex, double con, 
                  double intelligence, double wis, double charisma) {
        super(name, health, inventory, description, maxHealth, str, dex, con, intelligence, wis, charisma, ac);
        this.questDialogue = questDialogue;
        this.canGiveQuest = canGiveQuest;
        this.damage = damage;
        this.moralityFlag = moralityFlag;
    }

    public Adversary actAsAdversary() {
        return new Adversary(this);
    }

    public void giveQuest() {
        if (canGiveQuest) {
            //TODO: figure this out much later, when i'm adding quests.
            //TODO: decide whether this even belongs in Neutral.java
        }
        System.out.println(questDialogue.get(0));
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

    public double getMoralityFlag() {
        return moralityFlag;
    }
}
