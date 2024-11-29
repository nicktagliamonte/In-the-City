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
                  double Intelligence, double wis, double charisma, double alignmentImpact) {
        super(name, health, inventory, description, maxHealth, str, dex, con, Intelligence, wis, charisma, ac, alignmentImpact);
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
        }
        System.out.println(questDialogue.get(0));
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
