package com.nicktagliamonte.rooms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class Adjacency {
    @Expose private String type;    // e.g., "door" or "stairs"
    @Expose private String coordinates;
    @Expose private String description;
    @Expose private transient String adjoiningRoomName; // Used for initial deserialization only
    @Expose private Room adjoiningRoom; // Actual Room object for gameplay use
    @Expose private boolean isStairsUp;
    @Expose private String lockType;
    @Expose private boolean isLocked;
    @Expose private int baseChances;
    @Expose private int difficulty;
    @Expose private int combination;
    @Expose private int dexScore;

    //pickable lock
    public Adjacency(String type, String coordinates, String description, String adjoiningRoomName, boolean isStairsUp, 
                        String lockType, boolean isLocked, int baseChances, int difficulty, int dexScore) {
        this.type = type;
        this.coordinates = coordinates;
        this.description = description;
        this.adjoiningRoomName = adjoiningRoomName;
        this.isStairsUp = isStairsUp;
        this.lockType = lockType;
        this.isLocked = isLocked;
        this.baseChances = baseChances;
        this.difficulty = difficulty;
        this.dexScore = dexScore;
    }

    //combination lock
    public Adjacency(String type, String coordinates, String description, String adjoiningRoomName,
            boolean isStairsUp, String lockType, boolean isLocked, int combination, int dexScore) {
        this.type = type;
        this.coordinates = coordinates;
        this.description = description;
        this.adjoiningRoomName = adjoiningRoomName;
        this.isStairsUp = isStairsUp;
        this.lockType = lockType;
        this.isLocked = isLocked;
        this.combination = combination;
        this.dexScore = dexScore;
    }

    //no lock, or inactive lock
    public Adjacency(String type, String coordinates, String description, String adjoiningRoomName,
            boolean isStairsUp, String lockType, boolean isLocked, int dexScore) {
        this.type = type;
        this.coordinates = coordinates;
        this.description = description;
        this.adjoiningRoomName = adjoiningRoomName;
        this.isStairsUp = isStairsUp;
        this.lockType = lockType;
        this.isLocked = isLocked;
        this.dexScore = dexScore;
    }

    public int getdexScore() {
        return dexScore;
    }

    public void setCombination(int combination) {
        this.combination = combination;
    }

    public int getCombination() {
        return combination;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean getIsLocked() {
        return isLocked;
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

    public String getLockType() {
        return lockType;
    }

    public void setLockType(String lockType) {
        this.lockType = lockType;
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

    public String toSerializableFormat() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("coordinates", coordinates);
        jsonObject.addProperty("description", description);
        jsonObject.addProperty("isStairsUp", isStairsUp);
        jsonObject.addProperty("lockType", lockType);
        jsonObject.addProperty("isLocked", isLocked);
        jsonObject.addProperty("baseChances", baseChances);
        jsonObject.addProperty("difficulty", difficulty);
        jsonObject.addProperty("combination", combination);
        jsonObject.addProperty("dexScore", dexScore);
    
        // Manually add the adjoining room's name if the Room object is present
        if (adjoiningRoom != null) {
            jsonObject.addProperty("adjoiningRoom", adjoiningRoom.getName());
        } else {
            jsonObject.addProperty("adjoiningRoom", "");
        }
    
        // Use Gson for pretty-printing the JsonObject
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }
}
