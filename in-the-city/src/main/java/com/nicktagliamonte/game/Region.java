package com.nicktagliamonte.game;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.rooms.Room;

public class Region {
    @Expose private List<Room> rooms;
    @Expose private String regionName;
    @Expose private boolean hasSafeZone = false;

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

    public boolean getHasSafeZone() {
        return hasSafeZone;
    }

    public void setHasSafeZone(boolean hasSafeZone) {
        this.hasSafeZone = hasSafeZone;
    }
}