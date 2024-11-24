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
                JsonObject inventoryJson = jsonObject.getAsJsonObject("inventory");
                List<Item> inventory = new ArrayList<>();
                for (Map.Entry<String, JsonElement> entry : inventoryJson.entrySet()) {
                    Item item = context.deserialize(entry.getValue(), Item.class);
                    inventory.add(item);
                }
                String description = jsonObject.get("description").getAsString();
                double maxHealth = jsonObject.get("maxHealth").getAsDouble();
                List<String> hints = context.deserialize(jsonObject.getAsJsonArray("hints"), List.class);
                List<String> questDialogue = context.deserialize(jsonObject.getAsJsonArray("questDialogue"), List.class);
                boolean canGiveQuest = jsonObject.get("canGiveQuest").getAsBoolean();

                return new Friend(name, health, inventory, description, maxHealth, hints, questDialogue, canGiveQuest);
            case "adversary":
                String adversaryName = jsonObject.get("name").getAsString();
                double adversaryHealth = jsonObject.get("health").getAsDouble();
                JsonObject adversaryInventoryJson = jsonObject.getAsJsonObject("inventory");
                List<Item> adversaryInventory = new ArrayList<>();
                for (Map.Entry<String, JsonElement> entry : adversaryInventoryJson.entrySet()) {
                    Item item = context.deserialize(entry.getValue(), Item.class);
                    adversaryInventory.add(item);
                }
                String adversaryDescription = jsonObject.get("description").getAsString();
                double adversaryMaxHealth = jsonObject.get("maxHealth").getAsDouble();
                double adversaryDamage = jsonObject.get("damage").getAsDouble();
                double adversaryAc = jsonObject.get("ac").getAsDouble();
                double adversaryStr = jsonObject.get("str").getAsDouble();
                double adversaryDex = jsonObject.get("dex").getAsDouble();
                double adversaryCon = jsonObject.get("con").getAsDouble();
                double adversaryIntelligence = jsonObject.get("intelligence").getAsDouble();
                double adversaryWis = jsonObject.get("wis").getAsDouble();
                double adversaryCharisma = jsonObject.get("charisma").getAsDouble();

                return new Adversary(adversaryName, adversaryHealth, adversaryInventory, adversaryDescription, adversaryMaxHealth, adversaryDamage, adversaryAc,
                        adversaryStr, adversaryDex, adversaryCon, adversaryIntelligence, adversaryWis, adversaryCharisma);
            case "neutral":
                String neutralName = jsonObject.get("name").getAsString();
                double neutralHealth = jsonObject.get("health").getAsDouble();
                JsonObject neutralInventoryJson = jsonObject.getAsJsonObject("inventory");
                List<Item> neutralInventory = new ArrayList<>();
                for (Map.Entry<String, JsonElement> entry : neutralInventoryJson.entrySet()) {
                    Item item = context.deserialize(entry.getValue(), Item.class);
                    neutralInventory.add(item);
                }
                String neutralDescription = jsonObject.get("description").getAsString();
                double neutralMaxHealth = jsonObject.get("maxHealth").getAsDouble();
                List<String> neutralHints = context.deserialize(jsonObject.getAsJsonArray("hints"), List.class);
                List<String> neutralQuestDialogue = context.deserialize(jsonObject.getAsJsonArray("questDialogue"), List.class);
                boolean neutralCanGiveQuest = jsonObject.get("canGiveQuest").getAsBoolean();
                double neutralDamage = jsonObject.get("damage").getAsDouble();
                double moralityFlag = jsonObject.get("moralityFlag").getAsDouble();
                double neutralAc = jsonObject.get("ac").getAsDouble();
                double neutralStr = jsonObject.get("str").getAsDouble();
                double neutralDex = jsonObject.get("dex").getAsDouble();
                double neutralCon = jsonObject.get("con").getAsDouble();
                double neutralIntelligence = jsonObject.get("intelligence").getAsDouble();
                double neutralWis = jsonObject.get("wis").getAsDouble();
                double neutralCharisma = jsonObject.get("charisma").getAsDouble();

                return new Neutral(neutralName, neutralHealth, neutralInventory, neutralDescription, neutralMaxHealth, neutralHints, 
                                   neutralQuestDialogue, neutralCanGiveQuest, neutralDamage, moralityFlag, neutralAc, neutralStr, neutralDex, neutralCon,
                                   neutralIntelligence, neutralWis, neutralCharisma);
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
                double partyMemberDamage = jsonObject.get("damage").getAsDouble();
                String characterClassName = jsonObject.get("characterClass").getAsString();
                CharacterClass characterClass = new CharacterClass(characterClassName);

                return new PartyMember(partyMemberName, partyMemberInventory, partyMemberDescription, partyMemberHints, partyMemberDamage, characterClass);
            default:
                throw new JsonParseException("unknown npc type " + npcType);
        }
    }
}
