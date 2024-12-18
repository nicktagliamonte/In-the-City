package com.nicktagliamonte.items;

import com.nicktagliamonte.game.GameState;

public class Medical extends Item {
    public Medical() {
        super("Medical Pack", "Basic first aid, bandages, antiseptics, pain relievers. Essential for recovery and minor healing.", 3, true, 10, "", true);
    }

    @Override
    public void use(GameState gameState) {
        if (gameState.getPlayer().getHealth() + (gameState.getPlayer().getMaxHealth() / 10) > gameState.getPlayer().getMaxHealth()) {
            gameState.getPlayer().setHealth(gameState.getPlayer().getMaxHealth());
        } else {
            gameState.getPlayer().setHealth(gameState.getPlayer().getHealth() + (gameState.getPlayer().getMaxHealth() / 10));
        }
        System.out.println("The medical pack restores some health");
    }
}
