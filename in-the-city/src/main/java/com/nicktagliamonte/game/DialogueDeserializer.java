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

        // Iterate through each character in the JSON
        for (Map.Entry<String, JsonElement> characterEntry : jsonObject.entrySet()) {
            String characterName = characterEntry.getKey();
            JsonObject dialoguesJson = characterEntry.getValue().getAsJsonObject();
            Map<String, Dialogue> dialogues = new HashMap<>();

            // Iterate through each dialogue of the character
            for (Map.Entry<String, JsonElement> dialogueEntry : dialoguesJson.entrySet()) {
                String dialogueId = dialogueEntry.getKey();

                // Deserialize the dialogue entry and add to the dialogues map
                try {
                    Dialogue dialogue = context.deserialize(dialogueEntry.getValue(), Dialogue.class);
                    dialogues.put(dialogueId, dialogue);
                } catch (JsonParseException e) {
                    System.err.println("Error deserializing dialogue with ID: " + dialogueId);
                    throw new JsonParseException("Error deserializing dialogue", e);
                }
            }

            characterDialogues.put(characterName, dialogues);
        }

        return new RegionDialogue(characterDialogues);
    }
}