package com.nicktagliamonte.items;

import com.nicktagliamonte.game.GameState;

/**
 * Currently, this is just a test implementation of an item for proof of concept r/e the USE command
 */


public class FuelCell extends Item {
    public FuelCell() {
        super("Fuel Cell", "A small, powerful energy source.", 4.0);
    }

    @Override
    public void use(GameState gameState) {
        // TODO: Specific logic for using a Fuel Cell
        System.out.println("You use the fuel cell. It powers up the device.");
    }
}
