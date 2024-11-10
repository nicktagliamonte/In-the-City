package com.nicktagliamonte.characters;
import java.util.ArrayList;
import java.util.List;

import com.nicktagliamonte.items.Item;

public class Person {
    private String name;
    private double health;
    private double energy;
    private List<Item> inventory; // General inventory for all characters (could be used for NPCs)

    // Constructor, getters, setters, etc.
    public Person(String name) {
        this.name = name;
        this.inventory = new ArrayList<>();
    }

    public Person(String name, List<Item> inventory) {
        this.name = name;
        this.inventory = inventory;
    }

    public Person(String name, int health, int energy) {
        this.name = name;
        this.health = health;
        this.energy = energy;
        this.inventory = new ArrayList<>();
        System.out.println("here");
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
}
