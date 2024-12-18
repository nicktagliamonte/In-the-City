package com.nicktagliamonte.Spells;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class SpellDeserializer implements JsonDeserializer<Spell> {
    @Override
    public Spell deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (!jsonObject.has("type")) {
            throw new JsonParseException("Spell type is missing from the JSON");
        }

        String spellType = jsonObject.get("type").getAsString();

        switch (spellType) {
            case "IonSurge":
                return new IonSurge();
            // Add more cases for different spell types
            default:
                throw new JsonParseException("Unknown Spell Type: " + spellType);
        }
    }
        
}
