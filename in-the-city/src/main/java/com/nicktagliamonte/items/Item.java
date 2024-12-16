package com.nicktagliamonte.items;

import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.game.GameState;

public class Item {
    @Expose private String name;
    @Expose private String description;
    @Expose private double weight;
    @Expose private boolean isConsumable;
    @Expose private double value;
    @Expose private String puzzleType;
    @Expose private String dataPath;

    public Item(String name, String description, double weight, boolean isConsumable, double value, String puzzleType) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.isConsumable = isConsumable;
        this.value = value;
        this.puzzleType = puzzleType;
    }

    public Item(String name, String description, double weight, boolean isConsumable, double value, String puzzleType, String dataPath) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.isConsumable = isConsumable;
        this.value = value;
        this.puzzleType = puzzleType;
        this.dataPath = dataPath;
    }

    public String getDataPath() {
        return this.dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getPuzzleType() {
        return this.puzzleType;
    }

    public void setPuzzleType(String puzzleType) {
        this.puzzleType = puzzleType;
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