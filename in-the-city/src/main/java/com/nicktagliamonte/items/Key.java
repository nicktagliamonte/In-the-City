package com.nicktagliamonte.items;

public class Key extends Item {
    public Key(String name, String description, double weight, boolean isConsumable, int value) {
        super(name, description, weight, isConsumable, value, "", true);
    }
}
