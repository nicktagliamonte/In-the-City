package com.nicktagliamonte.characters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
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
            case "Friend":
            case "friend":
                String name = jsonObject.get("name").getAsString();
                double health = jsonObject.get("health").getAsDouble();
                JsonElement inventoryElement = jsonObject.get("inventory");
                JsonObject inventoryJsonObject = null;
                JsonArray inventoryJsonArray = null;
                List<Item> inventory = new ArrayList<>();

                if (inventoryElement != null) {
                    if (inventoryElement.isJsonObject()) {
                        inventoryJsonObject = inventoryElement.getAsJsonObject();
                        for (Map.Entry<String, JsonElement> entry : inventoryJsonObject.entrySet()) {
                            List<Item> items = context.deserialize(entry.getValue(), Item.class);
                            for (Item item : items) {
                                inventory.add(item);
                            }
                        }
                    } else if (inventoryElement.isJsonArray()) {
                        inventoryJsonArray = inventoryElement.getAsJsonArray();
                        for (JsonElement element : inventoryJsonArray) {
                            List<Item> items = context.deserialize(element, Item.class);
                            for (Item item : items) {
                                inventory.add(item);
                            }
                        }
                    } 
                }
                
                String description = jsonObject.get("description").getAsString();
                double maxHealth = jsonObject.get("maxHealth").getAsDouble();
                List<String> questDialogue = context.deserialize(jsonObject.getAsJsonArray("questDialogue"), List.class);
                boolean canGiveQuest = jsonObject.get("canGiveQuest").getAsBoolean();
                boolean friendDynamicDialogue = jsonObject.get("dynamicDialogue").getAsBoolean();

                return new Friend(name, health, inventory, description, maxHealth, questDialogue, canGiveQuest, friendDynamicDialogue);
            case "Adversary":
            case "adversary":
                String adversaryName = jsonObject.get("name").getAsString();
                double adversaryHealth = jsonObject.get("health").getAsDouble();
                JsonElement adversaryInventoryElement = jsonObject.get("inventory");
                JsonObject adversaryInventoryJsonObject = null;
                JsonArray adversaryInventoryJsonArray = null;
                List<Item> adversaryInventory = new ArrayList<>();

                if (adversaryInventoryElement != null) {
                    if (adversaryInventoryElement.isJsonObject()) {
                        adversaryInventoryJsonObject = adversaryInventoryElement.getAsJsonObject();
                        for (Map.Entry<String, JsonElement> entry : adversaryInventoryJsonObject.entrySet()) {
                            List<Item> items = context.deserialize(entry.getValue(), Item.class);
                            for (Item item : items) {
                                adversaryInventory.add(item);
                            }
                        }
                    } else if (adversaryInventoryElement.isJsonArray()) {
                        adversaryInventoryJsonArray = adversaryInventoryElement.getAsJsonArray();
                        for (JsonElement element : adversaryInventoryJsonArray) {
                            List<Item> items = context.deserialize(element, Item.class);
                            for (Item item : items) {
                                adversaryInventory.add(item);
                            }
                        }
                    } 
                }

                String adversaryDescription = jsonObject.get("description").getAsString();
                double adversaryMaxHealth = jsonObject.get("maxHealth").getAsDouble();
                double adversaryDamage = jsonObject.get("damage").getAsDouble();
                double adversaryAc = jsonObject.get("ac").getAsDouble();
                double adversaryStr = jsonObject.get("strength").getAsDouble();
                double adversaryDex = jsonObject.get("dexterity").getAsDouble();
                double adversaryCon = jsonObject.get("constitution").getAsDouble();
                double adversaryIntelligence = jsonObject.get("Intelligence").getAsDouble();
                double adversaryWis = jsonObject.get("wisdom").getAsDouble();
                double adversaryCharisma = jsonObject.get("charisma").getAsDouble();
                double adversaryAlignmentImpact = jsonObject.get("alignmentImpact").getAsDouble();
                JsonArray adversaryAlliesJson = jsonObject.getAsJsonArray("allies");
                List<String> adversaryAllies = new ArrayList<>();
                for (JsonElement allyElement : adversaryAlliesJson) {
                    adversaryAllies.add(allyElement.getAsString());
                }
                boolean adversaryDynamicDialogue = jsonObject.get("dynamicDialogue").getAsBoolean();

                return new Adversary(adversaryName, adversaryHealth, adversaryInventory, adversaryDescription, adversaryMaxHealth, adversaryDamage, adversaryAc,
                        adversaryStr, adversaryDex, adversaryCon, adversaryIntelligence, adversaryWis, adversaryCharisma, adversaryAlignmentImpact, adversaryAllies, 
                        adversaryDynamicDialogue);
            case "Neutral":
            case "neutral":
                String neutralName = jsonObject.get("name").getAsString();
                double neutralHealth = jsonObject.get("health").getAsDouble();
                JsonElement neutralInventoryElement = jsonObject.get("inventory");
                JsonObject neutralInventoryJsonObject = null;
                JsonArray neutralInventoryJsonArray = null;
                List<Item> neutralInventory = new ArrayList<>();

                if (neutralInventoryElement != null) {
                    if (neutralInventoryElement.isJsonObject()) {
                        neutralInventoryJsonObject = neutralInventoryElement.getAsJsonObject();
                        for (Map.Entry<String, JsonElement> entry : neutralInventoryJsonObject.entrySet()) {
                            List<Item> items = context.deserialize(entry.getValue(), Item.class);
                            for (Item item : items) {
                                neutralInventory.add(item);
                            }
                        }
                    } else if (neutralInventoryElement.isJsonArray()) {
                        neutralInventoryJsonArray = neutralInventoryElement.getAsJsonArray();
                        for (JsonElement element : neutralInventoryJsonArray) {
                            List<Item> items = context.deserialize(element, Item.class);
                            for (Item item : items) {
                                neutralInventory.add(item);
                            }
                        }
                    } 
                }

                String neutralDescription = jsonObject.get("description").getAsString();
                double neutralMaxHealth = jsonObject.get("maxHealth").getAsDouble();
                List<String> neutralQuestDialogue = context.deserialize(jsonObject.getAsJsonArray("questDialogue"), List.class);
                boolean neutralCanGiveQuest = jsonObject.get("canGiveQuest").getAsBoolean();
                double neutralDamage = jsonObject.get("damage").getAsDouble();
                double moralityFlag = jsonObject.get("moralityFlag").getAsDouble();
                double neutralAc = jsonObject.get("ac").getAsDouble();
                double neutralStr = jsonObject.get("strength").getAsDouble();
                double neutralDex = jsonObject.get("dexterity").getAsDouble();
                double neutralCon = jsonObject.get("constitution").getAsDouble();
                double neutralIntelligence = jsonObject.get("Intelligence").getAsDouble();
                double neutralWis = jsonObject.get("wisdom").getAsDouble();
                double neutralCharisma = jsonObject.get("charisma").getAsDouble();
                //NOTE!!! this is the impact that fighting this character will have on alignment, and calculating alignment delta works by treating this as a percent.
                //i.e. fighting Miles decreases alignment by 150% of the current value
                double neutralAlignmentImpact = jsonObject.get("alignmentImpact").getAsDouble();  
                boolean neutralDynamicDialogue = jsonObject.get("dynamicDialogue").getAsBoolean();  

                return new Neutral(neutralName, neutralHealth, neutralInventory, neutralDescription, neutralMaxHealth, 
                                   neutralQuestDialogue, neutralCanGiveQuest, neutralDamage, moralityFlag, neutralAc, neutralStr, neutralDex, neutralCon,
                                   neutralIntelligence, neutralWis, neutralCharisma, neutralAlignmentImpact, neutralDynamicDialogue);
            case "PartyMember":
            case "partyMember":
            case "Partymember":
            case "partymember":
                String partyMemberName = jsonObject.get("name").getAsString();
                JsonElement partyMemberInventoryElement = jsonObject.get("inventory");
                JsonObject partyMemberInventoryJsonObject = null;
                JsonArray partyMemberInventoryJsonArray = null;
                List<Item> partyMemberInventory = new ArrayList<>();

                if (partyMemberInventoryElement != null) {
                    if (partyMemberInventoryElement.isJsonObject()) {
                        partyMemberInventoryJsonObject = partyMemberInventoryElement.getAsJsonObject();
                        for (Map.Entry<String, JsonElement> entry : partyMemberInventoryJsonObject.entrySet()) {
                            List<Item> items = context.deserialize(entry.getValue(), Item.class);
                            for (Item item : items) {
                                partyMemberInventory.add(item);
                            }
                        }
                    } else if (partyMemberInventoryElement.isJsonArray()) {
                        partyMemberInventoryJsonArray = partyMemberInventoryElement.getAsJsonArray();
                        for (JsonElement element : partyMemberInventoryJsonArray) {
                            List<Item> items = context.deserialize(element, Item.class);
                            for (Item item : items) {
                                partyMemberInventory.add(item);
                            }
                        }
                    } 
                }
                String partyMemberDescription = jsonObject.get("description").getAsString();
                double partyMemberDamage = jsonObject.get("damage").getAsDouble();
                String characterClassName = jsonObject.get("characterClass").getAsString();
                CharacterClass characterClass = new CharacterClass(characterClassName);
                boolean partyMemberDynamicDialogue = jsonObject.get("dynamicDialogue").getAsBoolean();

                return new PartyMember(partyMemberName, partyMemberInventory, partyMemberDescription, partyMemberDamage, characterClass, partyMemberDynamicDialogue);
            default:
                throw new JsonParseException("unknown npc type " + npcType);
        }
    }
}
