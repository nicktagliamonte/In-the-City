package com.nicktagliamonte.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.game.GameState;

public class FoodRation extends Item {
    public FoodRation() {
        super("Food Ration", "Preserved and easily portable food items (canned goods, dried meats, grains).", 2.0, true, 5.0, "", true);
    }

    @Override
    public void use(GameState gameState) {
        gameState.getPlayer().timeSinceFood = 0;
        if (gameState.getPlayer().getHealth() + (gameState.getPlayer().getMaxHealth() / 20) > gameState.getPlayer().getMaxHealth()) {
            gameState.getPlayer().setHealth(gameState.getPlayer().getMaxHealth());
        } else {
            gameState.getPlayer().setHealth(gameState.getPlayer().getHealth() + (gameState.getPlayer().getMaxHealth() / 20));
        }
        System.out.println("You eat the food ration. It restores a little bit of health, and sate your hunger.");
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "FoodRation");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}