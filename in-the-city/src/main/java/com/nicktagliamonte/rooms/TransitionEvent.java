package com.nicktagliamonte.rooms;

import com.google.gson.annotations.Expose;

public class TransitionEvent {
    @Expose private String description;

    public TransitionEvent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
