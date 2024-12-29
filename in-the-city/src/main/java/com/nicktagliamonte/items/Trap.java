package com.nicktagliamonte.items;

import java.util.List;
import java.util.Map;

import com.nicktagliamonte.game.GameState;

public class Trap extends Item {
    Map<Item, Integer> cost;
    public transient GameState gameState;

    public Trap(String name, String description, double weight, boolean isConsumable, int value, GameState gameState) {
        super(name, description, weight, isConsumable, value, "", true);
        this.gameState = gameState;
    }

    public Trap(String name, String description, double weight, boolean isConsumable, int value) {
        super(name, description, weight, isConsumable, value, "", true);
    }

    public void setCost(Map<Item, Integer> cost) {
        this.cost = cost;
    }

    public Map<Item, Integer> getCost() {
        return cost;
    }

    public void setTrap(String coordinates) {
        gameState.getPlayer().removeItemFromInventory(this);
        gameState.getCurrentRoom().addItemToRoom(coordinates, this);
    }

    public void springTrap(List<Item> items) {
        System.out.println("A trap you set has been sprung. The following items have been added to your safe zone inventory:");
        for (Item item : items) {
            System.out.println(item.getName());
        }
        gameState.safeZoneInventory.addListOfItemsToInventory(items);
    }

    public void expire() {
        gameState.getCurrentRoom().removeItemFromRoom(this.getName());
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
