package com.nicktagliamonte.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.characters.Conveyance;
import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.game.GameState;

public class FuelCell extends Item {
    public FuelCell() {
        super("Fuel Cell", "A small, powerful energy source.", 4.0, true, 20, "", true);
    }

    @Override
    public void use(GameState gameState) {
        if (gameState.getCurrentParty() != null) {
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    System.out.println("You fuel up the conveyance. It seems to take the fuel eagerly. Like it was hungry.");
                    ((Conveyance) npc).timeSinceFuel = 0;
                    ((Conveyance) npc).setInHunger(false);
                    gameState.getPlayer().removeItem(this);
                    return;
                }
            }
        }
        System.out.println("You don't have anything to use this on.");
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "FuelCell");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}