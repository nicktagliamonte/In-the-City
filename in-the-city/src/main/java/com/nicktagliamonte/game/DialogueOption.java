package com.nicktagliamonte.game;

import com.google.gson.annotations.Expose;

public class DialogueOption {
    @Expose private String text;
    @Expose private String nextDialogueId;
    @Expose private double impact;

    public DialogueOption(String text, String nextDialogueId, double impact) {
        this.text = text;
        this.nextDialogueId = nextDialogueId;
        this.impact = impact;
    }

    public String getText() {
        return text;
    }

    public String getNextDialogueId() {
        return nextDialogueId;
    }

    public double getImpact() {
        return impact;
    }
}