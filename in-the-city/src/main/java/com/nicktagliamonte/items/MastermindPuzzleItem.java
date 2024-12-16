package com.nicktagliamonte.items;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.game.GameState;

public class MastermindPuzzleItem extends Item{
    @Expose private String name;
    @Expose private String description;
    @Expose private double weight;
    @Expose private boolean isConsumable;
    @Expose private double value;
    @Expose private String puzzleType;
    @Expose private String dataPath;

    public MastermindPuzzleItem(String name, String description, double weight, boolean isConsumable, double value, String puzzleType, String dataPath) {
        super(name, description, weight, isConsumable, value, puzzleType);
        this.dataPath = dataPath;
    }

    public void use(GameState gameState) {
        gameState.launchMastermindPuzzle(dataPath);
    }

    public String getDataPath() {
        return dataPath;
    }
}
