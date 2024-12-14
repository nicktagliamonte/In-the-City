package com.nicktagliamonte.quests;

import com.google.gson.annotations.Expose;

public class Objective {
    @Expose private String id;
    @Expose private String type;  // ONLY "dialogue", "combat", "item", "puzzle".  adding more means changing at LEAST the commands in menu.java
    @Expose private String target;
    @Expose private int amount;  // For collect-type objectives
    @Expose private boolean isCompleted;

    public Objective(String id, String type, String target, int amount, boolean isCompleted) {
        this.id = id;
        this.type = type;
        this.target = target;
        this.amount = amount;
        this.isCompleted = isCompleted;  // Default status
    }

    public Objective() {
        this.id = "";
        this.type = "";
        this.target = "";
        this.amount = 0;
        this.isCompleted = false;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public int getAmount() {
        return amount;
    }
}