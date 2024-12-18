package com.nicktagliamonte.game;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.rooms.Room;

public class Region {
    @Expose private List<Room> rooms;
    @Expose private String regionName;
    @Expose private boolean hasSafeZone = false;
    @Expose private String[] transitionText;

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

    public String[] getTransitionText() {
        return transitionText;
    }

    public void setTransitionText(String[] transitionText) {
        this.transitionText = transitionText;
    }

    public void transition() {
        for (int i = 0; i < transitionText.length; i++) {
            if (i == 0) {
                displaySlow();
                i += 2;
            }
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(transitionText[i]);
        }
    }

    public void displaySlow() {
        char[] chars = transitionText[0].toCharArray();
        for (char letter : chars) {
            System.out.print(letter);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\n" + transitionText[1]);
    }
}