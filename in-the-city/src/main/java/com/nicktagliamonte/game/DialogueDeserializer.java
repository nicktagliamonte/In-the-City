package com.nicktagliamonte.game;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.Map;

public class DialogueDeserializer implements JsonDeserializer<RegionDialogue> {

    @Override
    public RegionDialogue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Deserialize dialogues with the correct type reference
        Type dialogueMapType = new com.google.gson.reflect.TypeToken<Map<String, Dialogue>>() {}.getType();
        Map<String, Dialogue> dialogues = context.deserialize(jsonObject.get("dialogues"), dialogueMapType);

        return new RegionDialogue(dialogues);
    }
}