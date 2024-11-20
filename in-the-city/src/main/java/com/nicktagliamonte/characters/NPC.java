package com.nicktagliamonte.characters;

import java.util.List;

import com.nicktagliamonte.items.Item;

public class NPC extends Person {
    private String description;
    private List<String> hints;
    private double maxHealth;
    private int deathSavingThrows;

    public NPC(String name, double health, double energy, List<Item> inventory, String description, double maxHealth,  List<String> hints) {
        super(name, maxHealth, energy, inventory);
        this.description = description;
        this.maxHealth = maxHealth;
        this.deathSavingThrows = 0;
        this.hints = hints;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDead() {
        if (this.getHealth() <= -maxHealth) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDown() {
        if (super.getHealth() > -maxHealth && super.getHealth() <= 0) {
            return true;
        } else {
            return false;
        }
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
        } else {
            System.out.println(super.getName() + " took " + amount + " points of damage, but is still alive");
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

    public String getHint() {
        if (hints.isEmpty()) {
            return (this.getName() + " has no hints to offer.");
        } else {
            return hints.get((int) (Math.random() * hints.size()));
        }
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
