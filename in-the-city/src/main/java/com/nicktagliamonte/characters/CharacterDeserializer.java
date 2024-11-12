package com.nicktagliamonte.characters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nicktagliamonte.items.Item;

public class CharacterDeserializer implements JsonDeserializer<NPC>{
    @Override
    public NPC deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (!jsonObject.has("type")) {
            throw new JsonParseException("NPC type is missing from the JSON");
        }

        String npcType = jsonObject.get("type").getAsString();
        String name = jsonObject.get("name").getAsString();
        double health = jsonObject.get("health").getAsDouble();
        double energy = jsonObject.get("energy").getAsDouble();
        JsonObject inventoryJson = jsonObject.getAsJsonObject("inventory");
        List<Item> inventory = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : inventoryJson.entrySet()) {
            Item item = context.deserialize(entry.getValue(), Item.class);
            inventory.add(item);
        }
        String description = jsonObject.get("description").getAsString();
        double maxHealth = jsonObject.get("maxHealth").getAsDouble();
        List<String> dialogue = context.deserialize(jsonObject.getAsJsonArray("dialogue"), List.class);
        double shrewdness = jsonObject.get("shrewdness").getAsDouble();
        List<String> hints = context.deserialize(jsonObject.getAsJsonArray("hints"), List.class);
        List<String> barterSuccessDialogue = context.deserialize(jsonObject.getAsJsonArray("barterSuccessDialogue"), List.class);
        List<String> barterFailureDialogue = context.deserialize(jsonObject.getAsJsonArray("barterFailureDialogue"), List.class);
        List<String> questDialogue = context.deserialize(jsonObject.getAsJsonArray("questDialogue"), List.class);
        boolean canGiveQuest = jsonObject.get("canGiveQuest").getAsBoolean();

        switch (npcType) {
            case "friend":
                return new Friend(name, health, energy, inventory, description, 
                maxHealth, dialogue, shrewdness, hints, barterSuccessDialogue, 
                barterFailureDialogue, questDialogue, canGiveQuest);
            //add new types in future updates
            default:
                throw new JsonParseException("unknown npc type " + npcType);
        }
    }
}
