package com.nicktagliamonte.items;

import java.io.File;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    @Expose private boolean interactable;

    public Item(String name, String description, double weight, boolean isConsumable, double value, String puzzleType, boolean isInteractable) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.isConsumable = isConsumable;
        this.value = value;
        this.puzzleType = puzzleType;
        this.interactable = isInteractable;        
    }

    public Item(String name, String description, double weight, boolean isConsumable, double value, String puzzleType, String dataPath) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.isConsumable = isConsumable;
        this.value = value;
        this.puzzleType = puzzleType;

        // Get the base directory (project's root directory)
        String baseDir = System.getProperty("user.dir");

        // Adjust dataPath based on puzzleType using relative paths
        if (puzzleType != null) {
            if (puzzleType.toLowerCase().contains("sequence")) {
                this.dataPath = baseDir + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator
                                + "resources" + File.separator + "json" + File.separator + "sequence puzzles" + File.separator + this.dataPath;
            } else if (puzzleType.toLowerCase().contains("mastermind")) {
                this.dataPath = baseDir + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator
                                + "resources" + File.separator + "json" + File.separator + "mastermind puzzles" + File.separator + this.dataPath;
            }
        }
    }

    public Item(String sequenceName, String sequenceDescription, double sequenceWeight, boolean sequenceIsConsumable,
            int sequenceValue, String puzzleType, String sequenceDataPath, boolean isInteractable) {
        this.name = sequenceName;
        this.description = sequenceDescription;
        this.weight = sequenceWeight;
        this.isConsumable =sequenceIsConsumable;
        this.value = sequenceValue;
        this.puzzleType = puzzleType;
        this.interactable = isInteractable;

        // Get the base directory (project's root directory)
        String baseDir = System.getProperty("user.dir");

        // Always add "sequence puzzles" to the front of dataPath in this constructor
        if (puzzleType != null && puzzleType.toLowerCase().contains("sequence")) {
            this.dataPath = baseDir + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator
                            + "resources" + File.separator + "json" + File.separator + "sequence puzzles" + File.separator + this.dataPath;
        }
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public boolean getInteractable() {
        return interactable;
    }

    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();

        jsonObject.addProperty("type", puzzleType);
        jsonObject.addProperty("quantity", 1);

        // Check if the dataPath is not empty
        if (dataPath != null && !dataPath.isEmpty()) {
            // Strip the path, keeping only the file name and extension
            String fileName = new File(dataPath).getName();
            jsonObject.addProperty("dataPath", fileName);
        } else {
            // Keep the dataPath as is if it's empty
            jsonObject.addProperty("dataPath", dataPath);
        }

        return gson.toJson(jsonObject);
    }
}