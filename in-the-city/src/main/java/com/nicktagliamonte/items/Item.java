package com.nicktagliamonte.items;

import com.nicktagliamonte.game.GameState;

public class Item {
    private String name;
    private String description;
    private double weight;
    private boolean isConsumable;
    private boolean isIntersectable;

    public Item(String name, String description, double weight, boolean isConsumable, boolean isIntersectable) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.isConsumable = isConsumable;
        this.isIntersectable = isIntersectable;
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

    public boolean getIsConsumable() {
        return isConsumable;
    }

    public boolean getIsIntersectable() {
        return isIntersectable;
    }

    public void setIsIntersectable(boolean isIntersectable) {
        this.isIntersectable = isIntersectable;
    }
    
    public void use(GameState gameState) {
        throw new UnsupportedOperationException("Subclasses must override use(GameState gameState)");
    }
}