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

        switch (npcType) {
            case "friend":
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
                double shrewdness = jsonObject.get("shrewdness").getAsDouble();
                List<String> hints = context.deserialize(jsonObject.getAsJsonArray("hints"), List.class);
                List<String> questDialogue = context.deserialize(jsonObject.getAsJsonArray("questDialogue"), List.class);
                boolean canGiveQuest = jsonObject.get("canGiveQuest").getAsBoolean();

                return new Friend(name, health, energy, inventory, description, 
                maxHealth, shrewdness, hints, questDialogue, canGiveQuest);
            case "adversary":
                String adversaryName = jsonObject.get("name").getAsString();
                double adversaryHealth = jsonObject.get("health").getAsDouble();
                double adversaryEnergy = jsonObject.get("energy").getAsDouble();
                JsonObject adversaryInventoryJson = jsonObject.getAsJsonObject("inventory");
                List<Item> adversaryInventory = new ArrayList<>();
                for (Map.Entry<String, JsonElement> entry : adversaryInventoryJson.entrySet()) {
                    Item item = context.deserialize(entry.getValue(), Item.class);
                    adversaryInventory.add(item);
                }
                String adversaryDescription = jsonObject.get("description").getAsString();
                double adversaryMaxHealth = jsonObject.get("maxHealth").getAsDouble();
                double adversaryDamage = jsonObject.get("damage").getAsDouble();
                double adversaryDex = jsonObject.get("dex").getAsDouble();
                double adversaryAc = jsonObject.get("ac").getAsDouble();

                return new Adversary(adversaryName, adversaryHealth, adversaryEnergy, adversaryInventory, adversaryDescription, 
                                     adversaryMaxHealth, adversaryDamage, adversaryDex, adversaryAc);
            case "neutral":
                String neutralName = jsonObject.get("name").getAsString();
                double neutralHealth = jsonObject.get("health").getAsDouble();
                double neutralEnergy = jsonObject.get("energy").getAsDouble();
                JsonObject neutralInventoryJson = jsonObject.getAsJsonObject("inventory");
                List<Item> neutralInventory = new ArrayList<>();
                for (Map.Entry<String, JsonElement> entry : neutralInventoryJson.entrySet()) {
                    Item item = context.deserialize(entry.getValue(), Item.class);
                    neutralInventory.add(item);
                }
                String neutralDescription = jsonObject.get("description").getAsString();
                double neutralMaxHealth = jsonObject.get("maxHealth").getAsDouble();
                double neutralShrewdness = jsonObject.get("shrewdness").getAsDouble();
                List<String> neutralHints = context.deserialize(jsonObject.getAsJsonArray("hints"), List.class);
                List<String> neutralQuestDialogue = context.deserialize(jsonObject.getAsJsonArray("questDialogue"), List.class);
                boolean neutralCanGiveQuest = jsonObject.get("canGiveQuest").getAsBoolean();
                double neutralAttackSpeed = jsonObject.get("attackSpeed").getAsDouble();
                double neutralDamage = jsonObject.get("damage").getAsDouble();
                double moralityFlag = jsonObject.get("moralityFlag").getAsDouble();

                return new Neutral(neutralName, neutralHealth, neutralEnergy, neutralInventory, neutralDescription, neutralMaxHealth, 
                                   neutralShrewdness, neutralHints, neutralQuestDialogue, neutralCanGiveQuest, neutralAttackSpeed, neutralDamage, moralityFlag);
            case "partymember":
                String partyMemberName = jsonObject.get("name").getAsString();
                JsonObject partyMemberInventoryJson = jsonObject.getAsJsonObject("inventory");
                List<Item> partyMemberInventory = new ArrayList<>();
                for (Map.Entry<String, JsonElement> entry : partyMemberInventoryJson.entrySet()) {
                    Item item = context.deserialize(entry.getValue(), Item.class);
                    partyMemberInventory.add(item);
                }
                String partyMemberDescription = jsonObject.get("description").getAsString();
                List<String> partyMemberHints = context.deserialize(jsonObject.getAsJsonArray("hints"), List.class);
                String characterClassName = jsonObject.get("characterClass").getAsString();
                CharacterClass characterClass = new CharacterClass(characterClassName);

                return new PartyMember(partyMemberName, partyMemberInventory, partyMemberDescription, partyMemberHints, characterClass);
            default:
                throw new JsonParseException("unknown npc type " + npcType);
        }
    }
}
