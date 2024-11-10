package com.nicktagliamonte.items;

import com.nicktagliamonte.game.GameState;

public class Item {
    private String name;
    private String description;
    private double weight;

    public Item(String name, String description, double weight) {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getWeight() {
        return weight;
    }
    
    public void use(GameState gameState) {
        throw new UnsupportedOperationException("Subclasses must override use(GameState gameState)");
    }
}