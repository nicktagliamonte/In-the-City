package com.nicktagliamonte.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.game.GameState;

public class Key extends Item {
    public Key(String name, String description, double weight, boolean isConsumable, int value) {
        super(name, description, weight, isConsumable, value, "", true);
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "Key");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }

    @Override
    public void use(GameState gameState) {
        System.out.println(super.getDescription());
    }
}
