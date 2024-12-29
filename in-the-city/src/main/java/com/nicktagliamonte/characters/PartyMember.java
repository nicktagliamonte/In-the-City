package com.nicktagliamonte.characters;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class PartyMember extends NPC {
    @Expose private CharacterClass characterClass;
    @Expose private double maxCarryWeight;
    @Expose private double damage;

    public PartyMember(String name, List<Item> inventory, String description, double damage, CharacterClass characterClass, boolean dynamicDialogue) {
        super(name, characterClass.getHealth(), inventory, description, characterClass.getHealth(),
                characterClass.getStrength(), characterClass.getDexterity(), characterClass.getConstitution(), characterClass.getIntelligence(), 
                characterClass.getWisdom(), characterClass.getCharisma(), characterClass.getAc(), 0, dynamicDialogue);
        this.damage = damage;
        this.characterClass = characterClass;
        this.maxCarryWeight = characterClass.getMaxCarryWeight();
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public String getClassName() {
        return characterClass.getClassName();
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    public void setMaxCarryWeight(double maxCarryWeight) {
        this.maxCarryWeight = maxCarryWeight;
    }

    public double getMaxCarryWeight() {
        return this.maxCarryWeight;
    }

    public void listInventory() {
        List<Item> inventory = super.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("No items in inventory");
        } else {
            Map<String, Long> itemCount = inventory.stream()
                    .collect(Collectors.groupingBy(Item::getName, Collectors.counting()));

            // Print items with their counts, formatted
            itemCount.forEach((name, count) -> 
                System.out.printf("%-15s %d%n", name, count));
        }
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();

        // Adding the type of the character class
        jsonObject.addProperty("type", "PartyMember");

        // Serializing the inventory items
        JsonArray serializedInventory = new JsonArray();
        for (Item item : super.getInventory()) {
            serializedInventory.add(JsonParser.parseString(item.toSerializableFormat()));
        }
        jsonObject.add("inventory", serializedInventory);

        // Adding the characterClass's class name instead of the whole object
        jsonObject.addProperty("characterClass", this.characterClass.getClassName());

        // Returning the JSON representation
        return gson.toJson(jsonObject);
    }
}