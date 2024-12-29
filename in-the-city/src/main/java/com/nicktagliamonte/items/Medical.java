package com.nicktagliamonte.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "Medical");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}
