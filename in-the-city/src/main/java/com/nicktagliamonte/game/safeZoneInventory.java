package com.nicktagliamonte.game;

import java.util.*;

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
            System.out.println("No items in safe zone inventory.");
        } else {
            inventory.forEach(item -> System.out.println(item.getName()));
        }
    }
}