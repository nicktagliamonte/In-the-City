package com.nicktagliamonte.items;

import com.nicktagliamonte.game.GameState;

public class FoodRation extends Item {
    public FoodRation() {
        super("Food Ration", "Preserved and easily portable food items (canned goods, dried meats, grains).", 2.0, true, 5.0, "");
    }

    @Override
    public void use(GameState gameState) {
        //logic to print a message indicatingthat the food was eaten, and to have some impact on hunger
        gameState.getPlayer().timeSinceFood = 0;
        System.out.println("You eat the food ration.");
    }
}