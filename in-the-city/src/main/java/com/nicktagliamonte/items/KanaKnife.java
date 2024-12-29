package com.nicktagliamonte.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.game.GameState;

public class KanaKnife extends Item {
    public KanaKnife() {
        super("Dull Knife", "A dull knife, its blade etched with kana characters, the wooden handle broken and splintered.", 0, false, 0, "", true);
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
        jsonObject.addProperty("type", "KanaKnife");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}