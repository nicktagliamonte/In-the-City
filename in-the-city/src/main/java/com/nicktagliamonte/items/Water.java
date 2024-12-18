package com.nicktagliamonte.items;

import com.nicktagliamonte.game.GameState;

public class Water extends Item {
    public Water() {
        super("Water", "Water that is potable...enough.", 0, true, 1.0, "", true);
    }

    @Override
    public void use(GameState gameState) {
        gameState.getPlayer().timeSinceWater = 0;
        System.out.println("You drink the water.");
    }
}