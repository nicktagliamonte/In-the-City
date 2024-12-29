package com.nicktagliamonte.items;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.game.GameState;
import com.nicktagliamonte.quests.Quest;

public class Plug extends Item {
    public Plug(String name, String description, double weight, boolean isConsumable, double value) {
        super(name, description, weight, isConsumable, value, "", false);
    }

    @Override
    public void use(GameState gameState) {
        // Check if the player has the required quest active
        List<Quest> activeQuests = gameState.getPlayer().getActiveQuests();
        for (Quest quest : activeQuests) {
            if (quest.getQuestId().equalsIgnoreCase("quest_003")) {
                super.setInteractable(true);
            }
        }

        if (super.getInteractable()) {
            super.setDescription("A power cord attached to the computer terminal, which has been plugged in.");
            System.out.println("The computer terminal is plugged in.");
            
            // Get the items in the current room
            Map<String, List<Item>> itemsInRoom = gameState.getCurrentRoom().getItemsInRoom();
            
            // Iterate through the locations and their associated item lists
            for (Map.Entry<String, List<Item>> entry : itemsInRoom.entrySet()) {
                for (Item item : entry.getValue()) {
                    if (item.getName().equalsIgnoreCase("computer terminal")) {
                        item.setInteractable(true);
                        System.out.println("The computer terminal is now interactable.");
                        return;
                    }
                }
            }
        } else {
            System.out.println("There is no power to the area yet. You leave the plug on the ground. Maybe there's someone you can talk to about this.");
        }
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "Plug");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}