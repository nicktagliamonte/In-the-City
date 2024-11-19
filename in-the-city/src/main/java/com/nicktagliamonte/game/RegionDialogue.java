package com.nicktagliamonte.game;

import java.util.List;
import java.util.Map;

public class RegionDialogue {
    private Map<String, Dialogue> dialogues;

    public RegionDialogue(Map<String, Dialogue> dialogues) {
        this.dialogues = dialogues;
    }

    public Dialogue getDialogue(String npcName) {
        return dialogues.getOrDefault(npcName, new Dialogue(npcName, "This character doesn't want to talk to you.", List.of(
                new DialogueOption("Good", "end"),
                new DialogueOption("Bad", "end"),
                new DialogueOption("End", "end"))));
    }
}