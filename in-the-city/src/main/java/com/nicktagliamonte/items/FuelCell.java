package com.nicktagliamonte.items;

public class FuelCell extends Item {
    public FuelCell() {
        super("Fuel Cell", "A small, powerful energy source.", 4.0, true, 5);
    }

    @Override
    public void use() {
        // TODO: Specific logic for using a Fuel Cell
        System.out.println("You use the fuel cell. It powers up the device.");
    }
}