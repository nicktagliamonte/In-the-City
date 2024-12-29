package com.nicktagliamonte.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.game.GameState;

public class Relic extends Item {
    public Relic() {
        super("", "", 5, false, 35, "", true);
        Map<String, String> itemDescriptions = new HashMap<>();
        itemDescriptions.put("Old Watch", "A worn-out timepiece from a bygone era.");
        itemDescriptions.put("Strange Jewelry", "A peculiar piece of jewelry, seemingly from another world.");
        itemDescriptions.put("Septa Token", "A small token with a confusing symbol, reading \"Good For One Fare\"");
        itemDescriptions.put("Faded Phone", "A phone with a cracked screen and long-dead battery.");
        itemDescriptions.put("Camera", "A camera, well past the time of being able to take pictures.");
        itemDescriptions.put("Damaged Headphones", "A pair of headphones, barely holding together.");

        Random random = new Random();
        List<Map.Entry<String, String>> entries = new ArrayList<>(itemDescriptions.entrySet());
        Map.Entry<String, String> randomEntry = entries.get(random.nextInt(entries.size()));

        String name = randomEntry.getKey();
        String description = randomEntry.getValue();
        
        super.setName(name);
        super.setDescription(description);
    }

    @Override
    public void use(GameState gameState) {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(super.getDescription());
        System.out.println("It doesn't look very useful to you, it seems like you could sell it for a good price.");
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "Relic");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}
