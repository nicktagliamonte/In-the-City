package com.nicktagliamonte.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.game.GameState;

public class Glasses extends Item {
    public Glasses() {
        super("Round-Lensed Glasses", "A pair of round-lensed glasses, one lens cracked, their frame bent.", 0, false, 0, "", true);
    }

    @Override
    public void use(GameState gameState) {
        System.out.println(getDescription());
        System.out.println("You don't have a use for this item. It doesn't weigh anything and you doubt anyone would pay you for it. But it's hard to imagine parting with it.");
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "Glasses");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}