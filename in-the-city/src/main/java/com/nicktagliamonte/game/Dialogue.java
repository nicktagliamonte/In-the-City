package com.nicktagliamonte.game;

import java.util.List;

public class Dialogue {
    private String npcName;
    private String npcLine;
    private List<DialogueOption> options;

    public Dialogue(String npcName, String npcLine, List<DialogueOption> options) {
        this.npcName = npcName;
        this.npcLine = npcLine;
        this.options = options;
    }

    public String getNpcName() {
        return npcName;
    }

    public String getNpcLine() {
        return npcLine;
    }

    public List<DialogueOption> getOptions() {
        return options;
    }
}