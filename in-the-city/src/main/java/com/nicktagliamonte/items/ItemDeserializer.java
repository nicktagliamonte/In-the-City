package com.nicktagliamonte.items;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ItemDeserializer implements JsonDeserializer<List<Item>> {
    @SuppressWarnings("unused")
    @Override
    public List<Item> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Check if "type" exists in the JSON object to avoid NullPointerException
        if (!jsonObject.has("type")) {
            throw new JsonParseException("Item type is missing from the JSON");
        }

        String itemType = jsonObject.get("type").getAsString();
        int quantity = jsonObject.has("quantity") ? jsonObject.get("quantity").getAsInt() : 1;
        boolean interactable = jsonObject.has("interactable") ? jsonObject.get("interactable").getAsBoolean() : true;

        List<Item> items = new ArrayList<>();

        // Handle different types based on the "type" field
        switch (itemType) {
            case "FuelCell":
                for (int i = 0; i < quantity; i++) {
                    items.add(new FuelCell());
                }
                return items;
            case "Armor":
                String name = jsonObject.get("name").getAsString();
                String description = jsonObject.get("description").getAsString();
                double weight = jsonObject.get("weight").getAsDouble();
                boolean isConsumable = jsonObject.get("consumable").getAsBoolean();
                int value = jsonObject.get("value").getAsInt();
                int acBonus = jsonObject.get("acbonus").getAsInt();
                int minLevel = jsonObject.get("minLevel").getAsInt();
                for (int i = 0; i < quantity; i++) {
                    items.add(new Armor(name, description, weight, value, acBonus, minLevel));
                }                
                return items;
            case "PuzzleBox":
                for (int i = 0; i < quantity; i++) {
                    items.add(new PuzzleBox());  
                }
                return items;
            case "Sequence Puzzle":
                String sequenceName = jsonObject.get("name").getAsString();
                String sequenceDescription = jsonObject.get("description").getAsString();
                double sequenceWeight = jsonObject.get("weight").getAsDouble();
                boolean sequenceIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int sequenceValue = jsonObject.get("value").getAsInt(); 
                String sequenceDataPath = jsonObject.get("dataPath").getAsString();
                for (int i = 0; i < quantity; i++) {
                    items.add(new Item(sequenceName, sequenceDescription, sequenceWeight, sequenceIsConsumable, sequenceValue, "sequence", sequenceDataPath, interactable));    
                }                
                return items;
            case "Mastermind Puzzle":
                String mastermindName = jsonObject.get("name").getAsString();
                String mastermindDescription = jsonObject.get("description").getAsString();
                double mastermindWeight = jsonObject.get("weight").getAsDouble();
                boolean mastermindIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int mastermindValue = jsonObject.get("value").getAsInt(); 
                String mastermindDataPath = jsonObject.get("dataPath").getAsString();
                for (int i = 0; i < quantity; i++) {
                    items.add(new MastermindPuzzleItem(mastermindName, mastermindDescription, mastermindWeight, mastermindIsConsumable, mastermindValue, "mastermind", mastermindDataPath));        
                }                
                return items;
            case "FoodRation":
                for (int i = 0; i < quantity; i++) {
                    items.add(new FoodRation());    
                }
                return items;
            case "Water":
                for (int i = 0; i < quantity; i++) {
                    items.add(new Water());
                }
                return items;
            case "Weapon":
                String weaponName = jsonObject.get("name").getAsString();
                String weaponDescription = jsonObject.get("description").getAsString();
                double weaponWeight = jsonObject.get("weight").getAsDouble();
                boolean weaponIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int weaponValue = jsonObject.get("value").getAsInt();
                String weaponDamage = jsonObject.get("damage").getAsString();
                for (int i = 0; i < quantity; i++) {
                    items.add(new Weapon(weaponName, weaponDescription, weaponWeight, weaponValue, weaponDamage));    
                }                
                return items;
            case "Plug":
                String itemName = jsonObject.get("name").getAsString();
                String itemDescription = jsonObject.get("description").getAsString();
                double itemWeight = jsonObject.get("weight").getAsDouble();
                boolean itemIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int itemValue = jsonObject.get("value").getAsInt();
                for (int i = 0; i < quantity; i++) {
                    items.add(new Plug(itemName, itemDescription, itemWeight, itemIsConsumable, itemValue));    
                }   
                return items;
            case "Scrap":
                for (int i = 0; i < quantity; i++) {
                    items.add(new Scrap());
                }
                return items;
            case "Relic":
                for (int i = 0; i < quantity; i++) {
                    items.add(new Relic());
                }
                return items;
            default:
                throw new JsonParseException("Unknown item type: " + itemType);
        }
    }
}