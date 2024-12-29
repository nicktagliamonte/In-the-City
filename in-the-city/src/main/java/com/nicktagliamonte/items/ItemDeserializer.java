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
                boolean isConsumable = (jsonObject.has("consumable") && !jsonObject.get("consumable").isJsonNull()) ? jsonObject.get("consumable").getAsBoolean() : false;
                int value = jsonObject.get("value").getAsInt();
                int acBonus = jsonObject.get("acBonus").getAsInt();
                int minLevel = jsonObject.get("minLevel").getAsInt();
                for (int i = 0; i < quantity; i++) {
                    items.add(new Armor(name, description, weight, value, acBonus, minLevel));
                }                
                return items;
            case "sequence":
            case "Sequence Puzzle":
                String sequenceName = jsonObject.get("name").getAsString();
                String sequenceDescription = jsonObject.get("description").getAsString();
                double sequenceWeight = jsonObject.get("weight").getAsDouble();
                boolean sequenceIsConsumable = (jsonObject.has("consumable") && !jsonObject.get("consumable").isJsonNull()) ? jsonObject.get("consumable").getAsBoolean() : false;
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
                boolean mastermindIsConsumable = (jsonObject.has("consumable") && !jsonObject.get("consumable").isJsonNull()) ? jsonObject.get("consumable").getAsBoolean() : false;
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
                boolean itemIsConsumable = (jsonObject.has("consumable") && !jsonObject.get("consumable").isJsonNull()) ? jsonObject.get("consumable").getAsBoolean() : false;

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
            case "Amulet":
                String amuletName = jsonObject.get("name").getAsString();
                String amuletDescription = jsonObject.get("description").getAsString();
                double amuletWeight = jsonObject.get("weight").getAsDouble();
                boolean amuletIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int amuletValue = jsonObject.get("value").getAsInt();
                int charBonus = jsonObject.get("charBonus").getAsInt();
                int amuletMinLevel = jsonObject.get("minLevel").getAsInt();
                for (int i = 0; i < quantity; i++) {
                    items.add(new Amulet(amuletName, amuletDescription, amuletWeight, amuletValue, charBonus, amuletMinLevel));
                }                
                return items;
            case "Belt":
                String beltName = jsonObject.get("name").getAsString();
                String beltDescription = jsonObject.get("description").getAsString();
                double beltWeight = jsonObject.get("weight").getAsDouble();
                boolean beltIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int beltValue = jsonObject.get("value").getAsInt();
                int conBonus = jsonObject.get("conBonus").getAsInt();
                int beltMinLevel = jsonObject.get("minLevel").getAsInt();
                for (int i = 0; i < quantity; i++) {
                    items.add(new Belt(beltName, beltDescription, beltWeight, beltValue, conBonus, beltMinLevel));
                }
                return items;            
            case "Boots":
                String bootsName = jsonObject.get("name").getAsString();
                String bootsDescription = jsonObject.get("description").getAsString();
                double bootsWeight = jsonObject.get("weight").getAsDouble();
                boolean bootsIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int bootsValue = jsonObject.get("value").getAsInt();
                int dexBonus = jsonObject.get("dexBonus").getAsInt();
                int bootsMinLevel = jsonObject.get("minLevel").getAsInt();
                for (int i = 0; i < quantity; i++) {
                    items.add(new Boots(bootsName, bootsDescription, bootsWeight, bootsValue, dexBonus, bootsMinLevel));
                }
                return items;            
            case "Gloves":
                String glovesName = jsonObject.get("name").getAsString();
                String glovesDescription = jsonObject.get("description").getAsString();
                double glovesWeight = jsonObject.get("weight").getAsDouble();
                boolean glovesIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int glovesValue = jsonObject.get("value").getAsInt();
                int strBonus = jsonObject.get("strBonus").getAsInt();
                int glovesMinLevel = jsonObject.get("minLevel").getAsInt();
                for (int i = 0; i < quantity; i++) {
                    items.add(new Gloves(glovesName, glovesDescription, glovesWeight, glovesValue, strBonus, glovesMinLevel));
                }
                return items;            
            case "HeadBand":
                String headbandName = jsonObject.get("name").getAsString();
                String headbandDescription = jsonObject.get("description").getAsString();
                double headbandWeight = jsonObject.get("weight").getAsDouble();
                boolean headbandIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int headbandValue = jsonObject.get("value").getAsInt();
                int intBonus = jsonObject.get("intBonus").getAsInt();
                int headbandMinLevel = jsonObject.get("minLevel").getAsInt();
                for (int i = 0; i < quantity; i++) {
                    items.add(new HeadBand(headbandName, headbandDescription, headbandWeight, headbandValue, intBonus, headbandMinLevel));
                }
                return items;            
            case "Ring":
                String ringName = jsonObject.get("name").getAsString();
                String ringDescription = jsonObject.get("description").getAsString();
                double ringWeight = jsonObject.get("weight").getAsDouble();
                boolean ringIsConsumable = jsonObject.get("consumable").getAsBoolean();
                int ringValue = jsonObject.get("value").getAsInt();
                int wisBonus = jsonObject.get("wisbonus").getAsInt();  // Replaced acBonus with wisBonus
                int ringMinLevel = jsonObject.get("minLevel").getAsInt();
                for (int i = 0; i < quantity; i++) {
                    items.add(new Ring(ringName, ringDescription, ringWeight, ringValue, wisBonus, ringMinLevel));
                }                
                return items;  
            case "KanaKnife":
                for (int i = 0; i < quantity; i++) {
                    items.add(new KanaKnife());
                }
                return items;
            case "Vali2":
                for (int i = 0; i < quantity; i++) {
                    items.add(new Vali2());
                }
                return items;
            case "CasioWatch":
                for (int i = 0; i < quantity; i++) {
                    items.add(new CasioWatch());
                }
                return items;
            case "Card":
                for (int i = 0; i < quantity; i++) {
                    items.add(new Card());
                }
                return items;
            case "Wallet":
                for (int i = 0; i < quantity; i++) {
                    items.add(new Wallet());
                }
                return items;
            case "Collar":
                for (int i = 0; i < quantity; i++) {
                    items.add(new Collar());
                }
                return items;
            case "Glasses":
                for (int i = 0; i < quantity; i++) {
                    items.add(new Glasses());
                }
                return items;
            case "WoodRing":
                for (int i = 0; i < quantity; i++) {
                    items.add(new WoodRing());
                }
                return items;
            case "AquamarineRing":
                for (int i = 0; i < quantity; i++) {
                    items.add(new AquamarineRing());
                }
                return items;
            case "BigMagicalTrap":
                int bmElapsedTime = jsonObject.get("elapsedTime").getAsInt();
                boolean bmSuccess = jsonObject.get("success").getAsBoolean();
                for (int i = 0; i < quantity; i++) {
                    BigMagicalTrap bmt = new BigMagicalTrap();
                    bmt.setSuccess(bmSuccess);
                    bmt.setElapsedTime(bmElapsedTime);
                    items.add(bmt);
                }
            case "SmallMagicalTrap":
                int smElapsedTime = jsonObject.get("elapsedTime").getAsInt();
                boolean smSuccess = jsonObject.get("success").getAsBoolean();
                for (int i = 0; i < quantity; i++) {
                    SmallMagicalTrap smt = new SmallMagicalTrap();
                    smt.setSuccess(smSuccess);
                    smt.setElapsedTime(smElapsedTime);
                    items.add(smt);
                }
            case "BigTrap":
                int bElapsedTime = jsonObject.get("elapsedTime").getAsInt();
                boolean bSuccess = jsonObject.get("success").getAsBoolean();
                for (int i = 0; i < quantity; i++) {
                    BigTrap bt = new BigTrap();
                    bt.setSuccess(bSuccess);
                    bt.setElapsedTime(bElapsedTime);
                    items.add(bt);
                }
            case "SmallTrap":
                int sElapsedTime = jsonObject.get("elapsedTime").getAsInt();
                boolean sSuccess = jsonObject.get("success").getAsBoolean();
                for (int i = 0; i < quantity; i++) {
                    SmallTrap st = new SmallTrap();
                    st.setSuccess(sSuccess);
                    st.setElapsedTime(sElapsedTime);
                    items.add(st);
                }
            case "Key": 
                String keyName = jsonObject.get("name").getAsString();
                String keyDescription = jsonObject.get("description").getAsString();
                double keyWeight = jsonObject.get("weight").getAsDouble();
                boolean keyIsConsumable = jsonObject.get("isConsumable").getAsBoolean();
                int keyValue = jsonObject.get("value").getAsInt();
                for (int i = 0; i < quantity; i++) {
                    items.add(new Key(keyName, keyDescription, keyWeight, keyIsConsumable, keyValue));
                }
            default:
                throw new JsonParseException("Unknown item type: " + itemType);
        }
    }
}