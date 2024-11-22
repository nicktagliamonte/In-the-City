package com.nicktagliamonte.characters;

import java.util.List;

import com.nicktagliamonte.items.Item;

public class Neutral extends NPC{
    private double shrewdness;
    private List<String> questDialogue;
    private boolean canGiveQuest;
    private double damage;
    private double moralityFlag;
    private double ac;

    public Neutral(String name, double health, List<Item> inventory, String description, double maxHealth,
                  double shrewdness, List<String> hints, List<String> questDialogue, boolean canGiveQuest,
                  double damage, double moralityFlag, double ac) {
        super(name, health, inventory, description, maxHealth, hints);
        this.shrewdness = shrewdness;
        this.questDialogue = questDialogue;
        this.canGiveQuest = canGiveQuest;
        this.damage = damage;
        this.moralityFlag = moralityFlag;
        this.ac = ac;
    }

    public Adversary actAsAdversary() {
        return new Adversary(this);
    }

    public double getAc() {
        return ac;
    }

    //TODO: since this is a neutral, update bartering AND SPEECH such that a sufficiently low player morality will cause this class to act as adversary
    public String initiateBarter(int playerCharisma) {
        return checkBarterSuccess(playerCharisma) ? "You have a deal." : "I just can't go that low, sorry.";
    }

    private boolean checkBarterSuccess(int playerCharisma) {
        return playerCharisma > this.shrewdness * Math.random();
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
