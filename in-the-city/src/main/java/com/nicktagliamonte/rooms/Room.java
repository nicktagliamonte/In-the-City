package com.nicktagliamonte.rooms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.items.Item;

//TODO: for story progression, there should be some kind of access control for certain rooms that equates to being too scared to go in

public class Room {
    private String name;
    private String description;
    private int width;
    private int height;
    private int playerX;
    private int playerY;
    private List<Adjacency> adjacentRooms;
    private Map<String, Item> itemsInRoom;
    private Map<String, NPC> peopleInRoom;
    private TransitionEvent transitionEvent;
    private boolean hasPlayer;
    private int[][] mask;
    private char[][] map;

    public Room(String name, String description, int width, int height, boolean hasPlayer, TransitionEvent transitionEvent, int[][] mask, int playerX, int playerY, char[][] map) {
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
        this.adjacentRooms = new ArrayList<Adjacency>();
        this.mask = mask;
        if (hasPlayer) {
            this.playerX = playerX;
            this.playerY = playerY;
        }
        this.map = map;
    }

    public int[][] getMask() {
        return mask;
    }

    public char[][] getMap() {
        return map;
    }

    public void updateMapEntry(char c, int xPos, int yPos) {
        map[(map.length - yPos - 1)][xPos] = c;
    }

    public void setAdjacencies(List<Adjacency> adjacentRooms) {
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
    }

    public void addPersonToRoom(String location, NPC person) {
        this.peopleInRoom.put(location, person);
    }

    public void triggerTransitionEvent() {
        System.out.println(transitionEvent.getDescription());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
    
            // Check if the next move is within the room bounds
            if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                if (mask[newY][newX] == 1) {
                    // Valid movement
                    playerX = newX;
                    playerY = newY;
    
                    // Check if the player has reached a square with an adjacency
                    for (Adjacency adjacency : adjacentRooms) {
                        String coords = adjacency.getCoordinates();  // e.g., "(4,9)"
                        String[] parts = coords.replace("(", "").replace(")", "").split(","); // Split by comma and remove parentheses
    
                        int adjX = Integer.parseInt(parts[0].trim());  // Parse the x coordinate
                        int adjY = Integer.parseInt(parts[1].trim());  // Parse the y coordinate
    
                        // If the player is standing on an adjacency, return the description message
                        if (adjX == playerX && adjY == playerY) {
                            return this; // Return the description of the adjacency
                        }
                    }
                } else {
                    return null; // Invalid movement (blocked by wall or other obstacle)
                }
            } else {
                // At the edge, check for adjacency transition
                return checkTransition();
            }
        }
        return this; // No transition, stayed in the same room
    }

    public void setPlayerPosition(int x, int y) {
        this.playerX = x;
        this.playerY = y;
    }

    private Room checkTransition() {
        String currentPositionKey = "(" + playerX + "," + playerY + ")";
    
        for (Adjacency adj : adjacentRooms) {
            if (adj.getCoordinates().equals(currentPositionKey)) {
                return adj.getAdjoiningRoom();
            }
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
        if (adjacentRooms == null || adjacentRooms.size() == 0) {
            System.out.println("no adjascent rooms");
            return;
        }
        System.out.println("Exits in this room: ");
        for (Adjacency adjacency : adjacentRooms) {
            System.out.println(adjacency.getDescription());
        }
    }

    public List<Adjacency> getAdjacentRooms() {
        return adjacentRooms;
    }

    public boolean hasPlayer() {
        return hasPlayer;
    }

    public void printMap() {
        for (char[] row : map) {
            for (char element : row) {
                System.out.print(element);
            }
            System.out.println();

            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}