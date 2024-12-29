package com.nicktagliamonte.rooms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Conveyance;
import com.nicktagliamonte.characters.Friend;
import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.Neutral;
import com.nicktagliamonte.characters.PartyMember;
import com.nicktagliamonte.game.GameState;
import com.nicktagliamonte.items.Item;

public class Room {
    @Expose private String name;
    @Expose private String description;
    @Expose private int width;
    @Expose private int height;
    @Expose private int playerX;
    @Expose private int playerY;
    @Expose private transient List<Adjacency> adjacentRooms;
    @Expose private Map<String, List<Item>> itemsInRoom;
    @Expose private Map<String, NPC> peopleInRoom;
    @Expose private TransitionEvent transitionEvent;
    @Expose private boolean hasPlayer;
    @Expose private int[][] mask;
    @Expose private char[][] map;
    @Expose private boolean isSafe;
    @Expose private boolean isEconomic;
    @Expose private String nextRoomToSafeZone;
    @Expose private boolean accessible;
    @Expose private String denialMessage;

    public Room(String name, String description, int width, int height, boolean hasPlayer, TransitionEvent transitionEvent, 
                int[][] mask, int playerX, int playerY, char[][] map, boolean isSafe, boolean isEconomic, String nextRoomToSafeZone,
                boolean accessible, String denialMessage) {
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
        this.isSafe = isSafe;
        this.isEconomic = isEconomic;
        this.nextRoomToSafeZone = nextRoomToSafeZone;
        this.accessible = accessible;
        this.denialMessage = denialMessage;
    }

    public void setAdjacentRooms(List<Adjacency> adjacentRooms) {
        this.adjacentRooms = adjacentRooms;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    public String getDenialMessage() {
        return denialMessage;
    }

    public void setDenialMessage(String denialMessage) {
        this.denialMessage = denialMessage;
    }

    public boolean getIsEconomic() {
        return isEconomic;
    }

    public void setIsEconomic(boolean isEconomic) {
        this.isEconomic = isEconomic;
    }

    public String getNextRoomToSafeZone() {
        return nextRoomToSafeZone;
    }

    public void setNextRoomToSafeZone(String nextRoomToSafeZone) {
        this.nextRoomToSafeZone = nextRoomToSafeZone;
    }

    public void setIsSafe(boolean isSafe, GameState gameState) {
        this.isSafe = isSafe;
        gameState.getCurrentRegion().setHasSafeZone(true);
    }

    public boolean getIsSafe() {
        return isSafe;
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

    public Map<String, List<Item>> getItemsInRoom() {
        return itemsInRoom;
    }

    public void setItemsInRoom(Map<String, List<Item>> itemsInRoom) {
        this.itemsInRoom = itemsInRoom;
    }

    public void removeItemFromRoom(String location) {
        this.itemsInRoom.remove(location);
    }

    public void addItemToRoom(String location, Item item) {
        // Check if the location already has a list of items
        List<Item> itemList = this.itemsInRoom.get(location);
    
        // If the list doesn't exist, create a new one
        if (itemList == null) {
            itemList = new ArrayList<>();
            this.itemsInRoom.put(location, itemList);  // Put the newly created list at the location
        }
    
        // Add the item to the list at the specified location
        itemList.add(item);
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
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(transitionEvent.getDescription());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Room movePlayer(String direction, int distance, double playerDex) {
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
                return checkTransition(playerDex);
            }
        }
        return this; // No transition, stayed in the same room
    }

    public void setPlayerPosition(int x, int y) {
        this.playerX = x;
        this.playerY = y;
    }

    private Room checkTransition(double playerDex) {
        String currentPositionKey = "(" + playerX + "," + playerY + ")";
    
        for (Adjacency adj : adjacentRooms) {
            if (adj.getCoordinates().equals(currentPositionKey)) {
                if (adj.getIsLocked()) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("The door is locked, and you do not have a key.");
                    return this;
                } else if (adj.getdexScore() > playerDex) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("You fail to access " + adj.getAdjoiningRoomName() + " because of a failed dex check. You cannot access the room right now.");
                    return this;
                }
                if (adj.getAdjoiningRoom().isAccessible()) {
                    return adj.getAdjoiningRoom();
                } else {
                    System.out.println(adj.getAdjoiningRoom().getDenialMessage());
                    return this;
                }
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
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("no adjascent rooms");
            return;
        }
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Exits in this room: ");
        for (Adjacency adjacency : adjacentRooms) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
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
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.print(element);
            }
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println();

            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public String toSerializableFormat() {
        JsonObject jsonObject = new JsonObject();
    
        // Serialize simple fields
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("description", description);
        jsonObject.addProperty("width", width);
        jsonObject.addProperty("height", height);
        jsonObject.addProperty("playerX", playerX);
        jsonObject.addProperty("playerY", playerY);
        jsonObject.addProperty("hasPlayer", hasPlayer);
        jsonObject.addProperty("isSafe", isSafe);
        jsonObject.addProperty("isEconomic", isEconomic);
        jsonObject.addProperty("nextRoomToSafeZone", nextRoomToSafeZone);
        jsonObject.addProperty("accessible", accessible);
        jsonObject.addProperty("denialMessage", denialMessage);
    
        // Serialize adjacency list
        JsonArray serializedAdjacency = new JsonArray();
        if (adjacentRooms != null) {
            for (Adjacency adj : adjacentRooms) {
                serializedAdjacency.add(JsonParser.parseString(adj.toSerializableFormat()));
            }
        }
        jsonObject.add("adjacentRooms", serializedAdjacency);
    
        // Serialize items in room
        JsonObject itemsMap = new JsonObject();
        if (itemsInRoom != null) {
            for (Map.Entry<String, List<Item>> entry : itemsInRoom.entrySet()) {
                JsonArray itemList = new JsonArray();
                for (Item item : entry.getValue()) {
                    itemList.add(JsonParser.parseString(item.toSerializableFormat()));
                }
                itemsMap.add(entry.getKey(), itemList);
            }
        }
        jsonObject.add("itemsInRoom", itemsMap);
    
        // Serialize people in room
        JsonObject peopleMap = new JsonObject();
        for(Map.Entry<String, NPC> entry : peopleInRoom.entrySet()) {
            if (entry.getValue() instanceof Adversary) {
                peopleMap.add(entry.getKey(), JsonParser.parseString(((Adversary) entry.getValue()).toSerializableFormat()).getAsJsonObject());
            } else if (entry.getValue() instanceof Conveyance) {
                peopleMap.add(entry.getKey(), JsonParser.parseString(((Conveyance) entry.getValue()).toSerializableFormat()).getAsJsonObject());
            } else if (entry.getValue() instanceof Friend) {
                peopleMap.add(entry.getKey(), JsonParser.parseString(((Friend) entry.getValue()).toSerializableFormat()).getAsJsonObject());
            } else if (entry.getValue() instanceof Neutral) {
                peopleMap.add(entry.getKey(), JsonParser.parseString(((Neutral) entry.getValue()).toSerializableFormat()).getAsJsonObject());
            } else {
                peopleMap.add(entry.getKey(), JsonParser.parseString(((PartyMember) entry.getValue()).toSerializableFormat()).getAsJsonObject());
            }
        }
        jsonObject.add("peopleInRoom", peopleMap);
    
        // Serialize transition event
        if (transitionEvent != null) {
            jsonObject.add("transitionEvent", JsonParser.parseString(transitionEvent.toSerializableFormat()).getAsJsonObject());
        }
    
        // Serialize mask
        if (mask != null) {
            JsonArray maskArray = new JsonArray();
            for (int[] row : mask) {
                JsonArray rowArray = new JsonArray();
                for (int cell : row) {
                    rowArray.add(cell);
                }
                maskArray.add(rowArray);
            }
            jsonObject.add("mask", maskArray);
        }
    
        // Serialize map
        if (map != null) {
            JsonArray mapArray = new JsonArray();
            for (char[] row : map) {
                JsonArray rowArray = new JsonArray();
                for (char cell : row) {
                    rowArray.add(String.valueOf(cell));
                }
                mapArray.add(rowArray);
            }
            jsonObject.add("map", mapArray);
        }
    
        // Use Gson for pretty-printing the final JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }    
}