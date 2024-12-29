package com.nicktagliamonte.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Player;
import com.nicktagliamonte.game.GameState;

public class Armor extends Item {
    @Expose private int acBonus;
    @Expose private boolean isEquipped;
    @Expose private int minLevel;

    public Armor(String name, String description, double weight, int value, int acBonus, int minLevel) {
        super(name, description, weight, false, value, "", true);
        this.acBonus = acBonus;
        this.minLevel = minLevel;
        isEquipped = false;
    }

    public void equip(Player player) {
        // Check if player already has armor equipped
        if (player.getArmor() != null) {
            System.out.println("You can only wear 1 armor item at a time. Unequip your current armor before equipping this one.");
            return;  // Exit early if armor is already equipped
        }
    
        // Check if the player meets the level requirement
        if (player.getLevel() < minLevel) {
            System.out.println("You are not high enough level to use this item. Save it for later, or sell it.");
            return;  // Exit early if player doesn't meet level requirements
        }
    
        // Equip the armor
        player.increaseAC(acBonus);
        player.setArmor(this);
        
        // Remove armor from inventory
        player.removeItemFromInventory(this);
    
        // Mark armor as equipped
        isEquipped = true;
    
        System.out.println("Successfully equipped " + super.getName());
    }    

    public void dequip(Player player) {
        if (!isEquipped) {
            System.out.println("That armor item is not currently equipped.");
            return;  // Exit early if the armor is not equipped
        }
    
        // Decrease player's AC and update states
        player.increaseAC(-acBonus);
        isEquipped = false;
        player.addItem(this);
        player.removeArmor();
    
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
        jsonObject.addProperty("type", "Armor");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}