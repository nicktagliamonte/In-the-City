package com.nicktagliamonte.items;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.game.GameState;

public class MastermindPuzzleItem extends Item{
    String mastermindDataPath;

    public MastermindPuzzleItem(String name, String description, double weight, boolean isConsumable, double value, String puzzleType, String dataPath) {
        super(name, description, weight, isConsumable, value, puzzleType, true);
        
        // Get the base directory (project's root directory)
        String baseDir = System.getProperty("user.dir");

        // Prepend the base path for mastermind puzzles if dataPath is provided
        if (dataPath != null && !dataPath.isEmpty()) {
            this.mastermindDataPath = baseDir + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator
                            + "resources" + File.separator + "json" + File.separator + "mastermind puzzles" + File.separator + dataPath;
        }
        
        super.setDataPath(this.mastermindDataPath);
    }    

    public void use(GameState gameState) {
        gameState.launchMastermindPuzzle(super.getDataPath());
    }

    public String getDataPath() {
        return super.getDataPath();
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        
        // Extract just the filename from dataPath if it's not empty
        if (this.mastermindDataPath != null && !this.mastermindDataPath.isEmpty()) {
            String fileName = new File(this.mastermindDataPath).getName(); // Get only the file name
            jsonObject.addProperty("dataPath", fileName); // Store only the filename in the 'dataPath' field
        }
    
        jsonObject.addProperty("type", "Mastermind Puzzle");
        jsonObject.addProperty("quantity", 1);
        
        return gson.toJson(jsonObject);
    }
}
