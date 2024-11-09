package com.nicktagliamonte.characters;
import java.util.ArrayList;
import java.util.List;

import com.nicktagliamonte.items.Item;

public class Person {
    private String name;
    private int health;
    private int energy;
    private List<Item> inventory; // General inventory for all characters (could be used for NPCs)

    public Person() {
    }

    // Constructor, getters, setters, etc.
    public Person(String name, int health, int energy) {
        this.name = name;
        this.health = health;
        this.energy = energy;
        this.inventory = new ArrayList<>();
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
}
