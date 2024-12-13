package com.nicktagliamonte.game;

import java.util.List;

import com.google.gson.annotations.Expose;

public class Dialogue {
    @Expose private String npcLine;
    @Expose private List<DialogueOption> options;

    // Getters
    public String getNpcLine() {
        return npcLine;
    }

    public List<DialogueOption> getOptions() {
        return options;
    }
}