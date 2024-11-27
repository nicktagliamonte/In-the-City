package com.nicktagliamonte.game;

public class DialogueOption {
    private String text;
    private String nextDialogueId;
    private double impact;

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