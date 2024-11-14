package com.nicktagliamonte.rooms;
// Room is basically a skin over linkedlist.  each room contains fields etc, among them are exits and a list of adjascent rooms

import java.util.HashMap;
import java.util.Map;

import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.items.Item;

public class Room {
    private String name;
    private String description;
    private int width;
    private int height;
    private int playerX;
    private int playerY;
    private Map<String, Room> adjacentRooms;
    private Map<String, Item> itemsInRoom;
    private Map<String, NPC> peopleInRoom;
    private TransitionEvent transitionEvent;
    private boolean hasPlayer;
    private int[][] mask;

    public Room(String name, String description, int width, int height, boolean hasPlayer, TransitionEvent transitionEvent, int[][] mask, int playerX, int playerY) {
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
        this.mask = mask;
        if (hasPlayer) {
            this.playerX = playerX;
            this.playerY = playerY;
        }
    }

    public int[][] getMask() {
        return mask;
    }

    public void setAdjacencies(Map<String, Room> adjacentRooms) {
        this.adjacentRooms = adjacentRooms;
    }

    public Map<String, Item> getItemsInRoom() {
        return itemsInRoom;
    }

    public void setItemsInRoom(Map<String, Item> itemsInRoom) {
        this.itemsInRoom = itemsInRoom;
    }

    public void removeItemFromRoom(String location) {
        this.itemsInRoom.remove(location);
    }

    public void addItemToRoom(String location, Item item) {
        this.itemsInRoom.put(location, item);
    }

    public Map<String, NPC> getPeopleInRoom() {
        return peopleInRoom;
    }

    public void setPeopleInRoom(Map<String, NPC> peopleInRoom) {
        this.peopleInRoom = peopleInRoom;
    }

    public void removePersonFromRoom(String location) {
        this.peopleInRoom.remove(location);
        //TODO: when implementing combat, have the attack which leads to an NPC death call this method with their location (similar to removeItemFromRoom method above)
    }
    //make dying remove the person from room, then work on the below

    public void addPersonToRoom(String location, NPC person) {
        this.peopleInRoom.put(location, person);
    }

    public void triggerTransitionEvent() {
        System.out.println(transitionEvent.getDescription());
    }

    public Room movePlayer(String direction, int distance) {
        int dx = 0, dy = 0;
        
        switch (direction.toUpperCase()) {
            case "NORTH":
            case "UP":
                dy = 1;
                break;
            case "SOUTH":
            case "DOWN":
                dy = -1;
                break;
            case "EAST":
            case "RIGHT":
                dx = 1;
                break;
            case "WEST":
            case "LEFT":
                dx = -1;
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    
        for (int step = 0; step < distance; step++) {
            int newX = playerX + dx;
            int newY = playerY + dy;
    
            if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                if (mask[newY][newX] == 1) {
                    playerX = newX;
                    playerY = newY;
    
                    Room transitionRoom = checkTransition();
                    if (transitionRoom != this) {
                        return transitionRoom;
                    }
                } else {
                    return null;
                }
            } else {
                return checkTransition();
            }
        }
        return this;
    }
    
    private Room checkTransition() {
        String currentPositionKey = "(" + playerX + "," + playerY + ")";
    
        if (adjacentRooms.containsKey(currentPositionKey)) {
            return adjacentRooms.get(currentPositionKey);
        }
    
        return this;
    }

    public String getPlayerPosition() {
        return "(" + playerX + ", " + playerY + ")";
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
}