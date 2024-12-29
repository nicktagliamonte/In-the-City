package com.nicktagliamonte.rooms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class TransitionEvent {
    @Expose private String description;

    public TransitionEvent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        return baseJson;
    }
}
