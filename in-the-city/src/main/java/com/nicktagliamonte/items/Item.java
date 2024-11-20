package com.nicktagliamonte.items;

import com.nicktagliamonte.game.GameState;

public class Item {
    private String name;
    private String description;
    private double weight;
    private boolean isConsumable;
    private int value;

    public Item(String name, String description, double weight, boolean isConsumable, int value) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.isConsumable = isConsumable;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public double getWeight() {
        return weight;
    }

    public boolean getIsConsumable() {
        return isConsumable;
    }
    
    public void use(GameState gameState) {
        throw new UnsupportedOperationException("Subclasses must override use(GameState gameState)");
    }
}