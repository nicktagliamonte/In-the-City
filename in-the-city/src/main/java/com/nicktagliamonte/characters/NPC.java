package com.nicktagliamonte.characters;

import java.util.List;

import com.nicktagliamonte.items.Item;

public class NPC extends Person {
    private String description;
    private double maxHealth;
    private int deathSavingThrows;
    private double strength;
    private double dexterity;
    private double constitution;
    private double intelligence;
    private double wisdom;
    private double charisma;
    private double ac;

    public NPC(String name, double health, List<Item> inventory, String description, double maxHealth,
                double strength, double dexterity, double constitution, double intelligence, double wisdom, double charisma, double ac) {
        super(name, maxHealth, inventory);
        this.description = description;
        this.maxHealth = maxHealth;
        this.deathSavingThrows = 0;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.ac = ac;

    }

    public NPC(String name, double health, List<Item> inventory, String description, double maxHealth) {
        super(name, maxHealth, inventory);
        this.description = description;
        this.maxHealth = maxHealth;
        this.deathSavingThrows = 0;
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
        return intelligence;
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

    public void setIntelligence(double intelligence) {
        this.intelligence = intelligence;
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

    public boolean isDead() {
        if (this.getHealth() <= -maxHealth) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDown() {
        return ((super.getHealth() > -maxHealth) && (super.getHealth() < 0));
    }

    public boolean isAlive() {
        return getHealth() > 0;
    }

    public void interact() {
        //TODO: placeholder
    }

    public void takeDamage(double amount) {
        super.setHealth(super.getHealth() - amount);
        if (isDead()) {
            System.out.println(super.getName() + " is fully dead.");
        } else if (isDown()) {
            System.out.println(super.getName() + " is down and making death saving throws.");
        }
    }

    public void heal(double amount) {
        super.setHealth(super.getHealth() + amount);
        if (isDead()) {
            System.out.println(super.getName() + " is still fully dead.");
        } else if (isDown()) {
            System.out.println(super.getName() + " is down and making death saving throws.");
        } else {
            System.out.println(super.getName() + " healed for  " + amount + ".");
        }
    }

    public void makeDeathSavingThrow() {
        int randomInt = (int) (Math.random() * 19);
        if (randomInt < 10) {
            deathSavingThrows -= 1;
            System.out.println(super.getName() + " is at " + deathSavingThrows + " on their death saving throws.");
        } else {
            deathSavingThrows += 1;
            System.out.println(super.getName() + " is at " + deathSavingThrows + " on their death saving throws.");
        }
        if (deathSavingThrows == 3) {
            super.setHealth(0);
            System.out.println(super.getName() + " is stabalized at 0 health.");
        } else if (deathSavingThrows == -3) {
            super.setHealth(this.maxHealth * -1);
            System.out.println(super.getName() + " is fully dead.");
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