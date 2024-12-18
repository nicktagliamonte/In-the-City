package com.nicktagliamonte.items;

import com.nicktagliamonte.game.GameState;

public class FuelCell extends Item {
    public FuelCell() {
        super("Fuel Cell", "A small, powerful energy source.", 4.0, true, 5, "", true);
    }

    @Override
    public void use(GameState gameState) {
        // TODO: Specific logic for using a Fuel Cell (which relies i think on having a conveyance?)
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("You use the fuel cell. It powers up the device.");
    }
}