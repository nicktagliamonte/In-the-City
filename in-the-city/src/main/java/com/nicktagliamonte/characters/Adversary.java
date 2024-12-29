package com.nicktagliamonte.characters;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class Adversary extends NPC {
    @Expose private double damage;
    @Expose private List<String> allies;

    // Primary constructor
    public Adversary(String name, double health, List<Item> inventory, String description, double maxHealth, double damage, double ac, double str, 
                     double dex, double con, double intelligence, double wis, double charisma, double alignmentImpact, List<String> allies, boolean dynamicDialogue) {
        super(name, health, inventory, description, maxHealth, str, dex, con, intelligence, wis, charisma, ac, alignmentImpact, dynamicDialogue);
        this.damage = damage;
        this.allies = allies;
    }

    // Copy constructor for converting from Neutral
    public Adversary(Neutral neutral) {
        this(neutral.getName(), neutral.getHealth(), neutral.getInventory(), neutral.getDescription(), neutral.getHealth(), neutral.getDamage(), 
             neutral.getAc(), neutral.getStrength(), neutral.getDexterity(), neutral.getConstitution(), neutral.getIntelligence(), neutral.getWisdom(), 
             neutral.getCharisma(), neutral.getAlignmentImpact(), new ArrayList<>(), neutral.getDynamicDialogue());
    }

    public List<String> getAllies() {
        return allies;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void takeDamage(double amount) {
        // Reduce health by damage amount
        this.setHealth(this.getHealth() - amount);
    }

    public boolean isDefeated() {
        // Return true if health is less than or equal to zero
        return this.getHealth() <= 0;
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();

        // Adding the type of the character class
        jsonObject.addProperty("type", "Adversary");

        // Serializing the inventory items
        JsonArray serializedInventory = new JsonArray();
        for (Item item : super.getInventory()) {
            serializedInventory.add(JsonParser.parseString(item.toSerializableFormat()));
        }
        jsonObject.add("inventory", serializedInventory);

        // Adding damage and allies properties
        jsonObject.addProperty("damage", this.damage);
        if (this.allies != null) {
            JsonArray alliesArray = new JsonArray();
            for (String ally : this.allies) {
                alliesArray.add(ally);
            }
            jsonObject.add("allies", alliesArray);
        }

        // Returning the JSON representation
        return gson.toJson(jsonObject);
    }
}