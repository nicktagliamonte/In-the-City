package com.nicktagliamonte.items;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ItemDeserializer implements JsonDeserializer<Item> {
    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Check if "type" exists in the JSON object to avoid NullPointerException
        if (!jsonObject.has("type")) {
            throw new JsonParseException("Item type is missing from the JSON");
        }

        String itemType = jsonObject.get("type").getAsString();  // Assumes a "type" field in the JSON
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        double weight = jsonObject.get("weight").getAsDouble();

        // Handle different types based on the "type" field
        switch (itemType) {
            case "FuelCell":
                return new FuelCell(name, description, weight);
            // Add more cases for different item types
            default:
                throw new JsonParseException("Unknown item type: " + itemType);
        }
    }
}