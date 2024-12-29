package com.nicktagliamonte.game;

import java.util.List;

import com.google.gson.annotations.Expose;

public class Dialogue {
    @Expose private String npcLine;
    @Expose private List<DialogueOption> options;

    // Constructor
    public Dialogue(String npcLine, List<DialogueOption> options) {
        if (npcLine == null || npcLine.isEmpty()) {
            throw new IllegalArgumentException("NPC line cannot be null or empty");
        }
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("Options cannot be null or empty");
        }
        this.npcLine = npcLine;
        this.options = options;
    }

    // Getters
    public String getNpcLine() {
        return npcLine;
    }

    public List<DialogueOption> getOptions() {
        return options;
    }

    // Optionally, you could add setters if needed
    public void setNpcLine(String npcLine) {
        this.npcLine = npcLine;
    }

    public void setOptions(List<DialogueOption> options) {
        this.options = options;
    }
}