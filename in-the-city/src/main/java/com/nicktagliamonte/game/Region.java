package com.nicktagliamonte.game;

import java.util.List;

import com.nicktagliamonte.rooms.Room;

public class Region {
    private List<Room> rooms;
    private String regionName;

    // Getters and setters
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}