package com.nicktagliamonte.game;

import java.util.Map;
import com.google.gson.annotations.Expose;

public class RegionDialogue {
    @Expose private Map<String, Map<String, Dialogue>> characterDialogues;

    // Constructor
    public RegionDialogue(Map<String, Map<String, Dialogue>> characterDialogues) {
        this.characterDialogues = characterDialogues;
    }

    // Get all dialogues for a character
    public Map<String, Dialogue> getDialogue(String characterName) {
        return characterDialogues.get(characterName);
    }
}