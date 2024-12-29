package com.nicktagliamonte.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Player;
import com.nicktagliamonte.game.GameState;

public class Gloves extends Item {
    @Expose private int strBonus;
    @Expose private boolean isEquipped;
    @Expose private int minLevel;

    public Gloves(String name, String description, double weight, int value, int strBonus, int minLevel) {
        super(name, description, weight, false, value, "", true);
        this.strBonus = strBonus;
        this.minLevel = minLevel;
        isEquipped = false;
    }

    public void equip(Player player) {
        // Check if player already has gloves equipped
        if (player.getGloves() != null) {
            System.out.println("You can only wear 1 pair of gloves item at a time. Unequip your current gloves before equipping this one.");
            return;  // Exit early if gloves is already equipped
        }
    
        // Check if the player meets the level requirement
        if (player.getLevel() < minLevel) {
            System.out.println("You are not high enough level to use this item. Save it for later, or sell it.");
            return;  // Exit early if player doesn't meet level requirements
        }
    
        // Equip the gloves
        player.setStrength(player.getStrength() + strBonus);
        player.setGloves(this);
        
        // Remove Gloves from inventory
        player.removeItemFromInventory(this);
    
        // Mark Gloves as equipped
        isEquipped = true;
    
        System.out.println("Successfully equipped " + super.getName());
    }    

    public void dequip(Player player) {
        if (!isEquipped) {
            System.out.println("Those gloves are not currently equipped.");
            return;  // Exit early if the gloves are not equipped
        }
    
        // Decrease player's AC and update states
        player.setStrength(player.getStrength() - strBonus);
        isEquipped = false;
        player.addItem(this);
        player.removeGloves();
    
        System.out.println("Successfully dequipped " + super.getName());
    }  
    
    @Override
    public void use(GameState gameState) {
        equip(gameState.getPlayer());
    }

    public int getMinLevel() {
        return minLevel;
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "Gloves");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}