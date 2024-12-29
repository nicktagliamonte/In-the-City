package com.nicktagliamonte.characters;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class Person {
    @Expose private String name;
    @Expose private double health;
    protected List<Item> inventory; // General inventory for all characters (could be used for NPCs)
    @Expose private int initiative;

    //constructor for the player character, custom attributes in Player.java subclass
    public Person(String name) {
        this.name = name;
        this.inventory = new ArrayList<>();
    }

    public Person(String name, double health, List<Item> inventory) {
        this.name = name;
        this.health = health;
        this.inventory = (inventory == null) ? new ArrayList<>() : inventory;
    }    

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public void removeItemByName(String itemName) {
        inventory.removeIf(item -> item.getName().equals(itemName));
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

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }
}
