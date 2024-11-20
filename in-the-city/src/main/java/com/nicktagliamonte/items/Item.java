package com.nicktagliamonte.items;

import java.util.Objects;

import com.nicktagliamonte.game.GameState;

public class Item {
    private String name;
    private String description;
    private double weight;
    private boolean isConsumable;
    private double value;

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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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

    public boolean equals(Item itemToCompare) {
        return this.name.equalsIgnoreCase(itemToCompare.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(item.getValue(), value) == 0 &&
            Double.compare(item.getWeight(), weight) == 0 &&
            name.equals(item.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, weight);
    }
}