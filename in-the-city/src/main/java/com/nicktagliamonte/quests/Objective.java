package com.nicktagliamonte.quests;

import com.google.gson.annotations.Expose;

class Objective {
    @Expose private String id;
    @Expose private String type;  // e.g., "defeat", "collect"
    @Expose private String target;
    @Expose private int amount;  // For collect-type objectives
    @Expose private String status;  // "incomplete", "complete"

    public Objective(String id, String type, String target, int amount) {
        this.id = id;
        this.type = type;
        this.target = target;
        this.amount = amount;
        this.status = "incomplete";  // Default status
    }

    public Objective() {
        this.id = "";
        this.type = "";
        this.target = "";
        this.amount = 0;
        this.status = "incomplete";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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