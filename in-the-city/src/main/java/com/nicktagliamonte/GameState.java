package com.nicktagliamonte;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {
    private Region currentRegion;
    private Room currentRoom;
    private Map<String, List<Item>> itemsInRooms;
    private Map<String, List<Person>> charactersInRooms;
    public Player player;
    public GameEngine gameEngine;

    // Constructor
    public GameState(Player player, GameEngine gameEngine, String regionFilePath, String adjacencyFilePath) {
        // Initialize the game state, including descriptions and room contents
        itemsInRooms = new HashMap<>();
        charactersInRooms = new HashMap<>();
        this.player = player;
        this.gameEngine = gameEngine;
        loadRegion(regionFilePath, adjacencyFilePath);
    }

    private void loadRegion(String regionFilePath, String adjacenciesFilePath) {
        Gson gson = new Gson();
        try (FileReader regionReader = new FileReader(regionFilePath)) {
            currentRegion = gson.fromJson(regionReader, Region.class);
            System.out.println("Region loaded: " + currentRegion.getRegionName());
            initializeCurrentRoom();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeCurrentRoom() {
        for (Room room : currentRegion.getRooms()) {
            if (room.hasPlayer()) {
                currentRoom = room;
            }
        }
    }

    public void updateRegion(String newRegionFilePath, String newAdjacencyFilePath) {
        loadRegion(newRegionFilePath, newAdjacencyFilePath);
    }

    public Region getCurrentRegion() {
        return currentRegion;
    }

    public Room getcurrentRoom() {
        return currentRoom;
    }

    public String getRoomDescription(String location) {
        return currentRoom.getDescription();
    }

    public List<Item> getItemsInCurrentRoom() {
        return itemsInRooms.getOrDefault(currentRoom, new ArrayList<>());
    }

    public List<Person> getCharactersInCurrentRoom() {
        return charactersInRooms.getOrDefault(currentRoom, new ArrayList<>());
    }

    public String changeLocation(String directionInput) {
        try {
            Direction direction = Direction.valueOf(directionInput.toUpperCase());
            Room newRoom = currentRoom.movePlayer(direction.toString());
    
            if (newRoom != null) {
                // Transition to the new room and update the current location
                currentRoom = newRoom;
                setCurrentRoom(newRoom); // Update game state to reflect the new room
                gameEngine.checkForRandomEvent();
                currentRoom.triggerTransitionEvent();
                return "You move " + directionInput.trim() + ". You have entered " + newRoom.getName();
            } else {
                // If there's no room to move into, stay at the current location
                return "you move " + directionInput + ". " + currentRoom.getPlayerPosition();
            }
        } catch (IllegalArgumentException e) {
            // If the input is not a valid direction, print an error message
            return "Invalid direction. Valid directions are: NORTH, EAST, SOUTH, WEST, UP, DOWN, LEFT, RIGHT.";
        }
    }    

    public void setCurrentRoom(Room room) {
        currentRoom = room;
    }

    public Player getPlayer() {
        return player;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public String getDirectionsToRegion() {
        //this is i think going to be a pathfinding operation.  this is going to be massively difficult to implement
        String ret = "working";
        return ret;
    }

    public String getDirectionsToEconomicZone() {
        //this is i think going to be a pathfinding operation.  this is going to be massively difficult to implement
        String ret = "working";
        return ret;
    }

    public String toSerializableFormat() {
        //this one is going to also be somewhat of an undertaking
        return "working";
    }

    public void fromSerializableFormat(String data) {
        // Parse the data and populate the game state
        // Example: Deserialize JSON or parse plain text to restore fields
        //this is a minor nightmare when it comes time.
    }

    public void enterDialogue(Person character) {
        //I'm not wholly clear on what this will look like
    }

    public void enterCombat(Person character) {
        //not wholly clear on this either
    }
}