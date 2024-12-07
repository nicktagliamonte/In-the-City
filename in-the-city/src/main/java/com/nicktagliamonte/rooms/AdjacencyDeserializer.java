package com.nicktagliamonte.rooms;

import com.google.gson.*;
import java.lang.reflect.Type;

public class AdjacencyDeserializer implements JsonDeserializer<Adjacency> {

    @Override
    public Adjacency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Deserialize the basic properties
        String type = jsonObject.get("type").getAsString();
        String coordinates = jsonObject.get("coordinates").getAsString();
        String description = jsonObject.get("description").getAsString();

        // The adjoiningRoom is just a string during initial deserialization
        String adjoiningRoomName = jsonObject.get("adjoiningRoom").getAsString();

        boolean isStairsUp = jsonObject.get("isStairsUp").getAsBoolean();
        boolean isLocked = jsonObject.get("isLocked").getAsBoolean();
        int baseChances = jsonObject.get("baseChances").getAsInt();
        int difficulty = jsonObject.get("difficulty").getAsInt();

        // Create and return an Adjacency object with just the name initially
        return new Adjacency(type, coordinates, description, adjoiningRoomName, isStairsUp, isLocked, baseChances, difficulty);
    }
}