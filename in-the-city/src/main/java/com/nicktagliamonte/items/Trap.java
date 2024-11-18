package com.nicktagliamonte.items;

import java.util.Map;

public class Trap extends Item {
    Map<Integer, Item> cost;

    public Trap(String name, String description, double weight, boolean isConsumable, Map<Integer, Item> cost) {
        super(name, description, weight, isConsumable);
        this.cost = cost;
    }

    public void springTrap(Map<Integer, Item> items) {
        //TODO: something to add the list of items to the inventory and then eliminate the item from the gamestate
    }

    public void expire() {
        //TODO: just destroy the trap. individual traps will call this super method after set amounts of time
    }
}
