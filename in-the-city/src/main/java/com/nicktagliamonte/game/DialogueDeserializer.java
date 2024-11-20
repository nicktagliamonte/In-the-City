package com.nicktagliamonte.game;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DialogueDeserializer implements JsonDeserializer<RegionDialogue> {

    @Override
    public RegionDialogue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Map<String, Map<String, Dialogue>> characterDialogues = new HashMap<>();

        for (Map.Entry<String, JsonElement> characterEntry : jsonObject.entrySet()) {
            String characterName = characterEntry.getKey();
            JsonObject dialoguesJson = characterEntry.getValue().getAsJsonObject();
            Map<String, Dialogue> dialogues = new HashMap<>();

            for (Map.Entry<String, JsonElement> dialogueEntry : dialoguesJson.entrySet()) {
                String dialogueId = dialogueEntry.getKey();
                Dialogue dialogue = context.deserialize(dialogueEntry.getValue(), Dialogue.class);
                dialogues.put(dialogueId, dialogue);
            }

            characterDialogues.put(characterName, dialogues);
        }

        return new RegionDialogue(characterDialogues);
    }
}