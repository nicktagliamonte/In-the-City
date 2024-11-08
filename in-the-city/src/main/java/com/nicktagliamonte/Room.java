package com.nicktagliamonte;
// Room is basically a skin over linkedlist.  each room contains fields etc, among them are exits and a list of adjascent rooms

import java.util.HashMap;
import java.util.Map;

public class Room {
    private String name;
    private String description;
    private int width;
    private int height;
    private int playerX; // Current X position of the player in the room
    private int playerY; // Current Y position of the player in the room
    private Map<String, Room> adjacentRooms;
    private TransitionEvent transitionEvent;

    public Room(String name, String description, int width, int height, TransitionEvent transitionEvent) {
        this.name = name;
        this.description = description;
        this.width = width;
        this.height = height;
        // Initialize player position, for example, starting at (0,0)
        this.playerX = 0;
        this.playerY = 0;
        this.adjacentRooms = new HashMap<>();
        this.transitionEvent = transitionEvent;
    }

    public void addAdjacentRoom(int x, int y, Room room) {
        adjacentRooms.put(x + "," + y, room);
    }

    public void triggerTransitionEvent() {
        System.out.println(transitionEvent.getDescription());
    }

    public Room movePlayer(String direction) {
        switch (direction.toUpperCase()) {
            case "NORTH":
                if (playerY < height - 1) {
                    playerY++;
                } else {
                    return checkTransition();
                }
                break;
            case "SOUTH":
                if (playerY > 0) {
                    playerY--;
                } else {
                    return checkTransition();
                }
                break;
            case "EAST":
                if (playerX < width - 1) {
                    playerX++;
                } else {
                    return checkTransition();
                }
                break;
            case "WEST":
                if (playerX > 0) {
                    playerX--;
                } else {
                    return checkTransition();
                }
            case "UP":
                if (playerY < height - 1) {
                    playerY++;
                } else {
                    return checkTransition();
                }
                break;
            case "DOWN":
                if (playerY > 0) {
                    playerY--;
                } else {
                    return checkTransition();
                }
                break;
            case "LEFT":
                if (playerX < width - 1) {
                    playerX++;
                } else {
                    return checkTransition();
                }
                break;
            case "RIGHT":
                if (playerX > 0) {
                    playerX--;
                } else {
                    return checkTransition();
                }
                break;
        }
        return null; // Movement was invalid (edge of room)
    }

    private Room checkTransition() {
        String key = playerX + "," + playerY;
        return adjacentRooms.get(key); // Return the adjacent room if there's one at this position
    }

    public String getPlayerPosition() {
        return "Current position: (" + playerX + ", " + playerY + ")";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void viewAdjascentRooms() {
        for (String key : adjacentRooms.keySet()) {
            System.out.println("There is an exit at " + key);
        }
    }
}