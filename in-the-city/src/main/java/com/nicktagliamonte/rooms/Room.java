package com.nicktagliamonte.rooms;
// Room is basically a skin over linkedlist.  each room contains fields etc, among them are exits and a list of adjascent rooms

import java.util.HashMap;
import java.util.Map;

import com.nicktagliamonte.items.Item;

public class Room {
    private String name;
    private String description;
    private int width;
    private int height;
    private int playerX; // Current X position of the player in the room
    private int playerY; // Current Y position of the player in the room
    private Map<String, Room> adjacentRooms;
    private Map<String, Item> itemsInRoom;
    private TransitionEvent transitionEvent;
    private boolean hasPlayer;

    public Room(String name, String description, int width, int height, boolean hasPlayer, TransitionEvent transitionEvent) {
        this.name = name;
        this.description = description;
        this.width = width;
        this.height = height;
        this.hasPlayer = hasPlayer;
        if (hasPlayer) {
            this.playerX = 0;
            this.playerY = 0;
        }
        this.transitionEvent = transitionEvent;
        this.adjacentRooms = new HashMap<String, Room>();
    }

    public void setAdjacencies(Map<String, Room> adjacentRooms) {
        this.adjacentRooms = adjacentRooms;
    }

    public void setItemsInRoom(Map<String, Item> itemsInRoom) {
        this.itemsInRoom = itemsInRoom;
    }

    public void triggerTransitionEvent() {
        System.out.println(transitionEvent.getDescription());
    }

    public Room movePlayer(String direction) {
        switch (direction.toUpperCase()) {
            case "NORTH":
            case "UP":
                if (playerY < height - 1) {
                    playerY++;
                } else {
                    return checkTransition(); // Check for adjacency at the boundary
                }
                break;
            case "SOUTH":
            case "DOWN":
                if (playerY > 0) {
                    playerY--;
                } else {
                    return checkTransition(); // Check for adjacency at the boundary
                }
                break;
            case "EAST":
            case "RIGHT":
                if (playerX < width - 1) {
                    playerX++;
                } else {
                    return checkTransition(); // Check for adjacency at the boundary
                }
                break;
            case "WEST":
            case "LEFT":
                if (playerX > 0) {
                    playerX--;
                } else {
                    return checkTransition(); // Check for adjacency at the boundary
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
        return this; // Return current room if movement is within bounds
    }
    
    // Check for adjacency at boundaries
    private Room checkTransition() {
        String currentPositionKey = "(" + playerX + "," + playerY + ")";
        return adjacentRooms.getOrDefault(currentPositionKey, null);
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
        if (adjacentRooms == null) {
            System.out.println("no adjascent rooms");
            return;
        }
        for (String key : adjacentRooms.keySet()) {
            System.out.println("There is an exit at " + key);
        }
    }

    public boolean hasPlayer() {
        return hasPlayer;
    }

    public Map<String, Item> getItemsInRoom() {
        return itemsInRoom;
    }
}