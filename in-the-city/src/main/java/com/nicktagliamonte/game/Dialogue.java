package com.nicktagliamonte.game;

import java.util.List;

public class Dialogue {
    private String npcLine;
    private List<DialogueOption> options;

    // Getters
    public String getNpcLine() {
        return npcLine;
    }

    public List<DialogueOption> getOptions() {
        return options;
    }
}