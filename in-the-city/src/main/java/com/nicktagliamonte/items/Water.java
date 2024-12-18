package com.nicktagliamonte.items;

import com.nicktagliamonte.game.GameState;

public class Water extends Item {
    public Water() {
        super("Water", "Preserved and easily portable food items (canned goods, dried meats, grains).", 0, true, 1.0, "");
    }

    @Override
    public void use(GameState gameState) {
        gameState.getPlayer().timeSinceWater = 0;
        System.out.println("You drink the water.");
    }
}