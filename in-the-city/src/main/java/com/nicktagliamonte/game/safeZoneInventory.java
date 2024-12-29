package com.nicktagliamonte.game;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.items.Item;

public class safeZoneInventory {
    public List<Item> inventory;

    public safeZoneInventory() {
        this.inventory = new ArrayList<>();
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    public void addListOfItemsToInventory(List<Item> items) {
        inventory.addAll(items);
    }

    public void removeItemFromInventory(Item item) {
        inventory.remove(item);
    }

    public Item getItemFromInventory(String itemName) {
        if (inventory.isEmpty()) {
            return null;
        } else {
            for (Item item : inventory) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    if (item.getIsConsumable()) {
                        removeItemFromInventory(item);
                    }
                    return item;
                }
            }
        }
        return null;
    }

    public void listInventory() {
        if (inventory.isEmpty()) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("No items in safe zone inventory.");
        } else {
            inventory.forEach(item -> System.out.println(item.getName()));
        }
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();

        JsonArray itemArray = new JsonArray();
        for (Item item : inventory) {
            itemArray.add(JsonParser.parseString(item.toSerializableFormat()));
        }
        jsonObject.add("safeZoneInventory", itemArray);
        
        return gson.toJson(jsonObject);
    }
}