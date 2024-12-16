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
        String lockType = jsonObject.get("lockType").getAsString();
        boolean isLocked = jsonObject.get("isLocked").getAsBoolean();
        int dexScore = jsonObject.get("dexScore").getAsInt();

        if (lockType.equalsIgnoreCase("combination")) {
            int combination = jsonObject.get("combination").getAsInt();
            return new Adjacency(type, coordinates, description, adjoiningRoomName, isStairsUp, lockType, isLocked, combination, dexScore);
        } else if (lockType.equalsIgnoreCase("pickable")) {
            int baseChances = jsonObject.get("baseChances").getAsInt();
            int difficulty = jsonObject.get("difficulty").getAsInt();
            return new Adjacency(type, coordinates, description, adjoiningRoomName, isStairsUp, lockType, isLocked, baseChances, difficulty, dexScore);
        } else {
            return new Adjacency(type, coordinates, description, adjoiningRoomName, isStairsUp, lockType, isLocked, dexScore);
        }
    }
}