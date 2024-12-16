package com.nicktagliamonte.items;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ItemDeserializer implements JsonDeserializer<Item> {
    @SuppressWarnings("unused")
    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Check if "type" exists in the JSON object to avoid NullPointerException
        if (!jsonObject.has("type")) {
            throw new JsonParseException("Item type is missing from the JSON");
        }

        String itemType = jsonObject.get("type").getAsString();

        // Handle different types based on the "type" field
        switch (itemType) {
            case "FuelCell":
                return new FuelCell();
            case "Armor":
                String name = jsonObject.get("name").getAsString();
                String description = jsonObject.get("description").getAsString();
                double weight = jsonObject.get("weight").getAsDouble();
                boolean isConsumable = jsonObject.get("consumable").getAsBoolean();
                int value = jsonObject.get("value").getAsInt();
                int acBonus = jsonObject.get("acbonus").getAsInt();
                return new Armor(name, description, weight, value, acBonus);
            case "PuzzleBox":
                return new PuzzleBox();
            case "Sequence Puzzle":
                String sequenceName = jsonObject.get("name").getAsString();
                String sequenceDescription = jsonObject.get("description").getAsString();
                double sequenceWeight = jsonObject.get("weight").getAsDouble();
                boolean sequenceIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int sequenceValue = jsonObject.get("value").getAsInt(); 
                String sequenceDataPath = jsonObject.get("dataPath").getAsString();
                return new Item(sequenceName, sequenceDescription, sequenceWeight, sequenceIsConsumable, sequenceValue, "sequence", sequenceDataPath);
            case "Mastermind Puzzle":
                String mastermindName = jsonObject.get("name").getAsString();
                String mastermindDescription = jsonObject.get("description").getAsString();
                double mastermindWeight = jsonObject.get("weight").getAsDouble();
                boolean mastermindIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int mastermindValue = jsonObject.get("value").getAsInt(); 
                String mastermindDataPath = jsonObject.get("dataPath").getAsString();
                return new MastermindPuzzleItem(mastermindName, mastermindDescription, mastermindWeight, mastermindIsConsumable, mastermindValue, "mastermind", mastermindDataPath);
            // Add more cases for different item types
            default:
                throw new JsonParseException("Unknown item type: " + itemType);
        }
    }
}