package com.nicktagliamonte.rooms;

import com.google.gson.annotations.Expose;

public class Adjacency {
    @Expose private String type;    // e.g., "door" or "stairs"
    @Expose private String coordinates;
    @Expose private String description;
    @Expose private transient String adjoiningRoomName; // Used for initial deserialization only
    @Expose private Room adjoiningRoom; // Actual Room object for gameplay use
    @Expose private boolean isStairsUp;
    @Expose private boolean isLocked;
    @Expose private int baseChances;
    @Expose private int difficulty;

    // Constructor, getters, and setters
    public Adjacency(String type, String coordinates, String description, String adjoiningRoomName, boolean isStairsUp, boolean isLocked, int baseChances, int difficulty) {
        this.type = type;
        this.coordinates = coordinates;
        this.description = description;
        this.adjoiningRoomName = adjoiningRoomName;
        this.isStairsUp = isStairsUp;
        this.isLocked = isLocked;
        this.baseChances = baseChances;
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getBaseChances() {
        return baseChances;
    }

    public void setBaseChances(int baseChances) {
        this.baseChances = baseChances;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Room getAdjoiningRoom() {
        return adjoiningRoom;
    }

    public void setAdjoiningRoom(Room adjoiningRoom) {
        this.adjoiningRoom = adjoiningRoom;
    }

    public String getAdjoiningRoomName() {
        return adjoiningRoomName;
    }

    public void setAdjoiningRoomName(String adjoiningRoomName) {
        this.adjoiningRoomName = adjoiningRoomName;
    }

    public boolean getIsStairsUp() {
        return isStairsUp;
    }
}
