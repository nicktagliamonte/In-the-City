package com.nicktagliamonte.characters;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class Neutral extends NPC {
    @Expose private transient List<String> questDialogue;
    @Expose private boolean canGiveQuest;
    @Expose private double damage;
    @Expose private double moralityFlag;

    // Constructor
    public Neutral(String name, double health, List<Item> inventory, String description, double maxHealth,
                  List<String> questDialogue, boolean canGiveQuest, double damage, double moralityFlag, 
                  double ac, double str, double dex, double con, double intelligence, double wis, double charisma, 
                  double alignmentImpact, boolean dynamicDialogue) {
        super(name, health, inventory, description, maxHealth, str, dex, con, intelligence, wis, charisma, ac, alignmentImpact, dynamicDialogue);
        this.questDialogue = questDialogue;
        this.canGiveQuest = canGiveQuest;
        this.damage = damage;
        this.moralityFlag = moralityFlag;
    }

    // Method to convert Neutral to Adversary
    public Adversary actAsAdversary() {
        return new Adversary(this);
    }

    // Getter and Setter for damage
    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    // Getter for moralityFlag
    public double getMoralityFlag() {
        return moralityFlag;
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
    
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
    
        // Adding the type of the character class
        jsonObject.addProperty("type", "Neutral");
    
        // Serializing the inventory items
        JsonArray serializedInventory = new JsonArray();
        for (Item item : super.getInventory()) {
            serializedInventory.add(JsonParser.parseString(item.toSerializableFormat()));
        }
        jsonObject.add("inventory", serializedInventory);
    
        // Adding questDialogue and canGiveQuest properties
        jsonObject.addProperty("canGiveQuest", this.canGiveQuest);
        if (this.questDialogue != null) {
            JsonArray questDialogueArray = new JsonArray();
            for (String dialogue : this.questDialogue) {
                questDialogueArray.add(dialogue);
            }
            jsonObject.add("questDialogue", questDialogueArray);
        }
    
        // Returning the JSON representation
        return gson.toJson(jsonObject);
    }    
}