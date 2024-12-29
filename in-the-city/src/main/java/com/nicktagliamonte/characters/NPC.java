package com.nicktagliamonte.characters;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.quests.Quest;

public class NPC extends Person {
    @Expose private String description;
    @Expose private double maxHealth;
    @Expose private int deathSavingThrows;
    @Expose private double strength;
    @Expose private double dexterity;
    @Expose private double constitution;
    @Expose private double Intelligence;
    @Expose private double wisdom;
    @Expose private double charisma;
    @Expose private double ac;
    @Expose private double alignmentImpact;
    @Expose private List<Quest> quests;
    @Expose private boolean dynamicDialogue;
    @Expose private String startNode = "start_1";

    public NPC(String name, double health, List<Item> inventory, String description, double maxHealth,
               double strength, double dexterity, double constitution, double Intelligence, double wisdom, 
               double charisma, double ac, double alignmentImpact, boolean dynamicDialogue) {
        super(name, maxHealth, inventory);
        this.description = description;
        this.maxHealth = maxHealth;
        this.deathSavingThrows = 0;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.Intelligence = Intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.ac = ac;
        this.alignmentImpact = alignmentImpact;
        this.quests = new ArrayList<>();
        this.dynamicDialogue = dynamicDialogue;
    }

    public NPC(String name, double health, List<Item> inventory, String description, double maxHealth, boolean dynamicDialogue) {
        super(name, maxHealth, inventory);
        this.description = description;
        this.maxHealth = maxHealth;
        this.deathSavingThrows = 0;
        this.dynamicDialogue = dynamicDialogue;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getDeathSavingThrows() {
        return deathSavingThrows;
    }

    public void setDeathSavingThrows(int deathSavingThrows) {
        this.deathSavingThrows = deathSavingThrows;
    }

    public String getStartNode() {
        return startNode;
    }

    public void setStartNode(String startNode) {
        this.startNode = startNode;
    }

    public boolean getDynamicDialogue() {
        return dynamicDialogue;
    }

    public void setDynamicDialogue(boolean dynamicDialogue) {
        this.dynamicDialogue = dynamicDialogue;
    }

    public void setQuests(List<Quest> quests) {
        this.quests = quests;
    }

    public void addQuests(Quest quest) {
        quests.add(quest);
    }

    public List<Quest> getQuests() {
        if (quests.isEmpty()) {
            System.out.println("There are no quests this character can give.");
            return null;
        }
        return quests;
    }

    public void giveQuest(Player player) {
        if (quests.isEmpty()) {
            System.out.println("There are no quests this character can give.");
            return;
        }
        for (Quest quest : quests) {
            if ("inactive".equals(quest.getStatus())) {
                player.addQuest(quest);
                System.out.println(getName() + " gives you the quest: " + quest.getTitle());
                break;
            }
        }
    }

    public double getAlignmentImpact() {
        return alignmentImpact;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public String getDescription() {
        return description;
    }

    public double getStrength() {
        return strength;
    }

    public double getDexterity() {
        return dexterity;
    }

    public double getConstitution() {
        return constitution;
    }

    public double getIntelligence() {
        return Intelligence;
    }

    public double getWisdom() {
        return wisdom;
    }

    public double getCharisma() {
        return charisma;
    }

    public double getAc() {
        return ac;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public void setDexterity(double dexterity) {
        this.dexterity = dexterity;
    }

    public void setConstitution(double constitution) {
        this.constitution = constitution;
    }

    public void setIntelligence(double Intelligence) {
        this.Intelligence = Intelligence;
    }

    public void setWisdom(double wisdom) {
        this.wisdom = wisdom;
    }

    public void setCharisma(double charisma) {
        this.charisma = charisma;
    }

    public void setAc(double ac) {
        this.ac = ac;
    }

    public void setAlignmentImpact(double alignmentImpact) {
        this.alignmentImpact = alignmentImpact;
    }

    public boolean isDead() {
        return this.getHealth() <= -maxHealth;
    }

    public boolean isDown() {
        return this.getHealth() > -maxHealth && this.getHealth() < 0;
    }

    public boolean isAlive() {
        return getHealth() > 0;
    }

    public void takeDamage(double amount) {
        super.setHealth(super.getHealth() - amount);
        handleStatusChange();
    }

    public void heal(double amount) {
        super.setHealth(super.getHealth() + amount);
        handleStatusChange();
    }

    private void handleStatusChange() {
        if (isDead()) {
            System.out.println(getName() + " is fully dead.");
        } else if (isDown()) {
            System.out.println(getName() + " is down and making death saving throws.");
        }
    }

    public void makeDeathSavingThrow() {
        int randomInt = (int) (Math.random() * 19);
        deathSavingThrows += randomInt < 10 ? -1 : 1;
        System.out.println(getName() + " is at " + deathSavingThrows + " on their death saving throws.");

        if (deathSavingThrows == 3) {
            setHealth(0);
            System.out.println(getName() + " is stabilized at 0 health.");
        } else if (deathSavingThrows == -3) {
            setHealth(-maxHealth);
            System.out.println(getName() + " is fully dead.");
        }
    }

    public int getAttackModifier() {
        return (int) Math.floor(strength);
    }

    public boolean isFriend() {
        return this instanceof Friend;
    }

    public boolean isAdversary() {
        return this instanceof Adversary;
    }

    public boolean isNeutral() {
        return this instanceof Neutral;
    }
}