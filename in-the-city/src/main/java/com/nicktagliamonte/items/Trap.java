package com.nicktagliamonte.items;

import java.util.List;
import java.util.Map;

import com.nicktagliamonte.game.GameState;

public class Trap extends Item {
    Map<Item, Integer> cost;
    GameState gameState;

    public Trap(String name, String description, double weight, boolean isConsumable, int value, GameState gameState) {
        super(name, description, weight, isConsumable, value);
        this.gameState = gameState;
    }

    public void setCost(Map<Item, Integer> cost) {
        this.cost = cost;
    }

    public Map<Item, Integer> getCost() {
        return cost;
    }

    public void setTrap(String coordinates) {
        List<Item> currentInventory = gameState.getPlayer().getInventory();
        currentInventory.remove(this);
        gameState.getPlayer().setInventory(currentInventory);
        gameState.getCurrentRoom().addItemToRoom(coordinates, this);
    }

    public void springTrap(List<Item> items) {
        //TODO: replace the below with something that adds the items to the SAFE ZONE or CONVEYANCE inventory and prints a message
        List<Item> currentInventory = gameState.getPlayer().getInventory();
        currentInventory.addAll(items);
        gameState.getPlayer().setInventory(currentInventory);
    }

    public void expire() {
        gameState.getCurrentRoom().removeItemFromRoom(this.getName());
    }
}
