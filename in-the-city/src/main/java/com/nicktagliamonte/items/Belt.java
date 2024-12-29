package com.nicktagliamonte.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Player;
import com.nicktagliamonte.game.GameState;

public class Belt extends Item {
    @Expose private int conBonus;
    @Expose private boolean isEquipped;
    @Expose private int minLevel;

    public Belt(String name, String description, double weight, int value, int conBonus, int minLevel) {
        super(name, description, weight, false, value, "", true);
        this.conBonus = conBonus;
        this.minLevel = minLevel;
        isEquipped = false;
    }

    public void equip(Player player) {
        // Check if player already has belt equipped
        if (player.getBelt() != null) {
            System.out.println("You can only wear 1 belt item at a time. Unequip your current belt before equipping this one.");
            return;  // Exit early if belt is already equipped
        }
    
        // Check if the player meets the level requirement
        if (player.getLevel() < minLevel) {
            System.out.println("You are not high enough level to use this item. Save it for later, or sell it.");
            return;  // Exit early if player doesn't meet level requirements
        }
    
        // Equip the belt
        player.setConstitution(player.getConstitution() + conBonus);
        player.setBelt(this);
        
        // Remove belt from inventory
        player.removeItemFromInventory(this);
    
        // Mark belt as equipped
        isEquipped = true;
    
        System.out.println("Successfully equipped " + super.getName());
    }    

    public void dequip(Player player) {
        if (!isEquipped) {
            System.out.println("That belt item is not currently equipped.");
            return;  // Exit early if the belt is not equipped
        }
    
        // Decrease player's AC and update states
        player.setConstitution(player.getConstitution() - conBonus);
        isEquipped = false;
        player.addItem(this);
        player.removeBelt();
    
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
        jsonObject.addProperty("type", "Belt");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}