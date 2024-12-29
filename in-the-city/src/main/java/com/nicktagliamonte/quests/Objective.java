package com.nicktagliamonte.quests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class Objective {
    @Expose private String id;
    @Expose private String type;  // ONLY "dialogue", "combat", "item", "puzzle", "stealth", "movement".  adding more means changing at LEAST the commands in menu.java
    @Expose private String target;
    @Expose private boolean isCompleted;
    @Expose private String completionMessage;

    public Objective(String id, String type, String target, boolean isCompleted, String completionMessage) {
        this.id = id;
        this.type = type;
        this.target = target;
        this.isCompleted = isCompleted;
        this.completionMessage = completionMessage;
    }

    public Objective() {
        this.id = "";
        this.type = "";
        this.target = "";
        this.isCompleted = false;
        this.completionMessage = null;
    }

    public void setCompletionMessage(String completionMessage) {
        this.completionMessage = completionMessage;
    }

    public String getCompletionMessage() {
        return completionMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}