package com.nicktagliamonte.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.game.GameState;

public class Water extends Item {
    public Water() {
        super("Water", "Water that is potable...enough.", 0, true, 1.0, "", true);
    }

    @Override
    public void use(GameState gameState) {
        gameState.getPlayer().timeSinceWater = 0;
        if (gameState.getPlayer().getHealth() + (gameState.getPlayer().getMaxHealth() / 25) > gameState.getPlayer().getMaxHealth()) {
            gameState.getPlayer().setHealth(gameState.getPlayer().getMaxHealth());
        } else {
            gameState.getPlayer().setHealth(gameState.getPlayer().getHealth() + (gameState.getPlayer().getMaxHealth() / 25));
        }
        System.out.println("You drink the water. It quenches your thirst, and makes you feel a bit better.");
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "Water");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}