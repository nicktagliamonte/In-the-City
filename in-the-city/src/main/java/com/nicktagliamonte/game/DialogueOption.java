package com.nicktagliamonte.game;

public class DialogueOption {
    private String text;
    private String nextDialogueId;

    public DialogueOption(String text, String nextDialogueId) {
        this.text = text;
        this.nextDialogueId = nextDialogueId;
    }

    public String getText() {
        return text;
    }

    public String getNextDialogueId() {
        return nextDialogueId;
    }
}