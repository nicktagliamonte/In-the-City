package com.nicktagliamonte.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Player;
import com.nicktagliamonte.game.GameState;

public class Boots extends Item {
    @Expose private int dexBonus;
    @Expose private boolean isEquipped;
    @Expose private int minLevel;

    public Boots(String name, String description, double weight, int value, int dexBonus, int minLevel) {
        super(name, description, weight, false, value, "", true);
        this.dexBonus = dexBonus;
        this.minLevel = minLevel;
        isEquipped = false;
    }

    public void equip(Player player) {
        // Check if player already has boots equipped
        if (player.getBoots() != null) {
            System.out.println("You can only wear 1 pair of boots item at a time. Unequip your current boots before equipping this one.");
            return;  // Exit early if boots is already equipped
        }
    
        // Check if the player meets the level requirement
        if (player.getLevel() < minLevel) {
            System.out.println("You are not high enough level to use this item. Save it for later, or sell it.");
            return;  // Exit early if player doesn't meet level requirements
        }
    
        // Equip the boots
        player.setDexterity(player.getDexterity() + dexBonus);
        player.setBoots(this);
        
        // Remove Boots from inventory
        player.removeItemFromInventory(this);
    
        // Mark Boots as equipped
        isEquipped = true;
    
        System.out.println("Successfully equipped " + super.getName());
    }    

    public void dequip(Player player) {
        if (!isEquipped) {
            System.out.println("Those boots are not currently equipped.");
            return;  // Exit early if the boots are not equipped
        }
    
        // Decrease player's AC and update states
        player.setDexterity(player.getDexterity() - dexBonus);
        isEquipped = false;
        player.addItem(this);
        player.removeBoots();
    
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
        jsonObject.addProperty("type", "Boots");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}