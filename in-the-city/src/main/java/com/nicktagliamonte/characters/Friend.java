package com.nicktagliamonte.characters;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class Friend extends NPC {
    @Expose private List<String> questDialogue;
    @Expose private boolean canGiveQuest;

    // Constructor
    public Friend(String name, double health, List<Item> inventory, String description, double maxHealth,
                  List<String> questDialogue, boolean canGiveQuest, boolean dynamicDialogue) {
        super(name, health, inventory, description, maxHealth, dynamicDialogue);
        this.questDialogue = questDialogue;
        this.canGiveQuest = canGiveQuest;
    }

    public boolean isCanGiveQuest() {
        return canGiveQuest;
    }

    // Getter and Setter for questDialogue
    public List<String> getQuestDialogue() {
        return questDialogue;
    }

    public void setQuestDialogue(List<String> questDialogue) {
        this.questDialogue = questDialogue;
    }

    // Getter and Setter for canGiveQuest
    public boolean canGiveQuest() {
        return canGiveQuest;
    }

    public void setCanGiveQuest(boolean canGiveQuest) {
        this.canGiveQuest = canGiveQuest;
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Convert the Friend object to a base JSON string
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();

        // Add the inventory items by calling toSerializableFormat on each item
        JsonArray inventoryArray = new JsonArray();
        for (Item item : super.getInventory()) {
            // Parse the serialized format as JSON and add it to the inventory array
            JsonObject itemJson = JsonParser.parseString(item.toSerializableFormat()).getAsJsonObject();
            inventoryArray.add(itemJson);
        }
        jsonObject.add("inventory", inventoryArray);
        jsonObject.addProperty("type", "Friend");

        // Return the final JSON as a string
        return gson.toJson(jsonObject);
    }
}