package com.nicktagliamonte.items;

import java.util.Map;

public class Trap extends Item {
    Map<Item, Integer> cost;

    public Trap(String name, String description, double weight, boolean isConsumable, int value) {
        super(name, description, weight, isConsumable, value);
    }

    public void setCost(Map<Item, Integer> cost) {
        this.cost = cost;
    }

    public Map<Item, Integer> getCost() {
        return cost;
    }

    public void springTrap(Map<Item, Integer> items) {
        //TODO: something to add the list of items to the inventory and then eliminate the item from the gamestate
    }

    public void expire() {
        //TODO: just destroy the trap. individual traps will call this super method after set amounts of time
    }
}
