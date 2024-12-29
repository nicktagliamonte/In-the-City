package com.nicktagliamonte.game;

import com.google.gson.annotations.Expose;

public class DialogueOption {
    @Expose private String text;
    @Expose private String nextDialogueId;
    @Expose private double impact;

    public DialogueOption(String text, String nextDialogueId, double impact) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty.");
        }
        if (nextDialogueId == null || nextDialogueId.isEmpty()) {
            throw new IllegalArgumentException("Next dialogue ID cannot be null or empty.");
        }
        if (impact < 0) {
            throw new IllegalArgumentException("Impact must be non-negative.");
        }

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

    @Override
    public String toString() {
        return "DialogueOption{" +
                "text='" + text + '\'' +
                ", nextDialogueId='" + nextDialogueId + '\'' +
                ", impact=" + impact +
                '}';
    }
}