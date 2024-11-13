package com.nicktagliamonte.game;

import com.nicktagliamonte.characters.*;
import com.nicktagliamonte.items.*;
import com.nicktagliamonte.rooms.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameState {
    private Region currentRegion;
    private Room currentRoom;
    public Player player;
    public GameEngine gameEngine;

    // Constructor
    public GameState(GameEngine gameEngine, String regionFilePath, String adjacencyFilePath, String itemsFilePath, String peopleFilePath) {
        // Initialize the game state, including descriptions and room contents
        this.gameEngine = gameEngine;
        initializePlayer();
        loadRegion(regionFilePath, adjacencyFilePath, itemsFilePath, peopleFilePath);
    }

    public void initializePlayer() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a name: ");
        String name = scanner.nextLine();
        CharacterClass characterClass;
        do {
            System.out.println("Enter a class (survivalist, technologist, or negotiator): ");
            String classInput = scanner.nextLine();
            characterClass = CharacterClass.createCharacterClass(classInput);
        } while (characterClass == null);
        
        this.player = new Player(name, characterClass);
        gameEngine.player = this.player;
    }

    private void loadRegion(String regionFilePath, String adjacenciesFilePath, String itemsFilePath, String peopleFilePath) {
        Gson gson = new Gson();
        try (FileReader regionReader = new FileReader(regionFilePath)) {
            currentRegion = gson.fromJson(regionReader, Region.class);
            System.out.println("Region loaded: " + currentRegion.getRegionName());
            loadAdjacencyList(adjacenciesFilePath);
            loaditemsInRoom(itemsFilePath);
            loadPeopleInRoom(peopleFilePath);
            System.out.println("loaded adjacent rooms");
            initializeCurrentRoom();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAdjacencyList(String adjacenciesFilePath) {
        Gson gson = new Gson();
        try (FileReader adjacenciesReader = new FileReader(adjacenciesFilePath)) {
            Type adjacencyMapType = new TypeToken<Map<String, Map<String, String>>>() {
            }.getType();
            JsonObject jsonObject = gson.fromJson(adjacenciesReader, JsonObject.class);
            JsonObject adjacencies = jsonObject.getAsJsonObject("adjacencies");
            Map<String, Map<String, String>> tempAdjacencyMap = gson.fromJson(adjacencies, adjacencyMapType);

            Map<String, Room> roomNameToRoomMap = new HashMap<>();
            for (Room room : currentRegion.getRooms()) {
                roomNameToRoomMap.put(room.getName(), room);
            }

            Map<Room, Map<String, Room>> adjacencyMap = new HashMap<>();

            for (Map.Entry<String, Map<String, String>> entry : tempAdjacencyMap.entrySet()) {
                String roomName = entry.getKey();
                Map<String, String> stringAdjacencies = entry.getValue();

                // Find the actual Room object by name
                Room currentRoom = roomNameToRoomMap.get(roomName);
                if (currentRoom != null) {
                    Map<String, Room> roomAdjacencies = new HashMap<>();

                    // Convert the String adjacencies to Room objects
                    for (Map.Entry<String, String> adjEntry : stringAdjacencies.entrySet()) {
                        String coordinates = adjEntry.getKey();
                        String adjacentRoomName = adjEntry.getValue();

                        Room adjacentRoom = roomNameToRoomMap.get(adjacentRoomName);
                        if (adjacentRoom != null) {
                            roomAdjacencies.put(coordinates, adjacentRoom);
                        }
                    }

                    // Set adjacencies for the current room
                    currentRoom.setAdjacencies(roomAdjacencies);
                    adjacencyMap.put(currentRoom, roomAdjacencies);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loaditemsInRoom(String itemsFilePath) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Item.class, new ItemDeserializer())
            .create();

        try (FileReader itemsReader = new FileReader(itemsFilePath)) {
            // Parse the JSON structure for rooms and their coordinates with item details
            JsonObject jsonObject = gson.fromJson(itemsReader, JsonObject.class);
            JsonObject itemsJson = jsonObject.getAsJsonObject("items");

            Map<String, Map<String, Item>> tempItemsMap = new HashMap<>();

            // Iterate over each room and parse item data
            for (Map.Entry<String, JsonElement> roomEntry : itemsJson.entrySet()) {
                String roomName = roomEntry.getKey();
                JsonObject roomItemsJson = roomEntry.getValue().getAsJsonObject();
                Map<String, Item> itemsMap = new HashMap<>();

                // Iterate over each coordinate and parse item info
                for (Map.Entry<String, JsonElement> coordinateEntry : roomItemsJson.entrySet()) {
                    String coordinates = coordinateEntry.getKey();
                    JsonObject itemJson = coordinateEntry.getValue().getAsJsonObject();

                    // Deserialize the item using the custom ItemDeserializer
                    Item item = gson.fromJson(itemJson, Item.class); // Deserialize item at the coordinate level

                    // Add the item to the map for the current room
                    itemsMap.put(coordinates, item);
                }

                // Add this room's item map to the main map
                tempItemsMap.put(roomName, itemsMap);
            }

            // Create a mapping of room names to actual Room objects
            Map<String, Room> roomNameToRoomMap = new HashMap<>();
            for (Room room : currentRegion.getRooms()) {
                roomNameToRoomMap.put(room.getName(), room);
            }

            // Set the items in each room
            for (Map.Entry<String, Map<String, Item>> roomEntry : tempItemsMap.entrySet()) {
                String roomName = roomEntry.getKey();
                Map<String, Item> itemsInRoom = roomEntry.getValue();

                // Find the actual Room object by name
                Room currentRoom = roomNameToRoomMap.get(roomName);
                
                if (currentRoom != null) {
                    currentRoom.setItemsInRoom(itemsInRoom); // Set items for this room
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPeopleInRoom(String peopleFilePath) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Item.class, new ItemDeserializer())
            .registerTypeAdapter(NPC.class, new CharacterDeserializer())
            .create();

        try (FileReader peopleReader = new FileReader(peopleFilePath)) {
            JsonObject jsonObject = gson.fromJson(peopleReader, JsonObject.class);
            JsonObject peopleJson = jsonObject.getAsJsonObject("people");

            Map<String, Map<String, NPC>> tempPeopleMap = new HashMap<>();

            for (Map.Entry<String, JsonElement> roomEntry : peopleJson.entrySet()) {
                String roomName = roomEntry.getKey();
                JsonObject roomPeopleJson = roomEntry.getValue().getAsJsonObject();
                Map<String, NPC> peopleMap = new HashMap<>();

                for (Map.Entry<String, JsonElement> coordinateEntry : roomPeopleJson.entrySet()) {
                    String coordinates = coordinateEntry.getKey();
                    JsonObject personJson = coordinateEntry.getValue().getAsJsonObject();

                    NPC npc = gson.fromJson(personJson, NPC.class);

                    peopleMap.put(coordinates, npc);
                }

                tempPeopleMap.put(roomName, peopleMap);
            }

            Map<String, Room> roomNameToRoomMap = new HashMap<>();
            for (Room room : currentRegion.getRooms()) {
                roomNameToRoomMap.put(room.getName(), room);
            }

            for (Map.Entry<String, Map<String, NPC>> roomEntry : tempPeopleMap.entrySet()) {
                String roomName = roomEntry.getKey();
                Map<String, NPC> peopleInRoom = roomEntry.getValue();

                Room currentRoom = roomNameToRoomMap.get(roomName);
                
                if (currentRoom != null) {
                    currentRoom.setPeopleInRoom(peopleInRoom);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //specifically used for initializing the starting room
        //TODO: when i'm loading save games, i'll probably need to find a way to make it so that the last room which contained the player doesn't conflict with this
    private void initializeCurrentRoom() {
        for (Room room : currentRegion.getRooms()) {
            if (room.hasPlayer()) {
                currentRoom = room;
            }
        }
    }

    public void updateRegion(String newRegionFilePath, String newAdjacencyFilePath, String newItemsFilePath, String newPeopleFilePath) {
        loadRegion(newRegionFilePath, newAdjacencyFilePath, newItemsFilePath, newPeopleFilePath);
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

    public String changeLocation(String directionInput) {
        try {
            Direction direction = Direction.valueOf(directionInput.toUpperCase());
            Room newRoom = currentRoom.movePlayer(direction.toString());

            if (newRoom != null && newRoom != currentRoom) {
                // Transition to the new room and update the current location
                currentRoom = newRoom;
                setCurrentRoom(newRoom); // Update game state to reflect the new room
                gameEngine.checkForRandomEvent();
                currentRoom.triggerTransitionEvent();
                return "You move " + directionInput.trim() + ". You have entered " + newRoom.getName();
            } else if (newRoom == currentRoom) {
                // Moved within the room but not transitioning
                return "You move " + directionInput + ". " + currentRoom.getPlayerPosition();
            } else {
                // Attempted to move out of the room without an adjacency
                return "You can't move further " + direction.toString().toLowerCase()
                        + ". There's no exit in that direction.";
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
        // this is i think going to be a pathfinding operation. this is going to be
        // massively difficult to implement
        String ret = "working";
        return ret;
    }

    public String getDirectionsToEconomicZone() {
        // this is i think going to be a pathfinding operation. this is going to be
        // massively difficult to implement
        String ret = "working";
        return ret;
    }

    public String toSerializableFormat() {
        // this one is going to also be somewhat of an undertaking
        return "working";
    }

    public void fromSerializableFormat(String data) {
        // Parse the data and populate the game state
        // Example: Deserialize JSON or parse plain text to restore fields
        // this is a minor nightmare when it comes time.
    }

    public void enterDialogue(Person character) {
        // I'm not wholly clear on what this will look like
    }

    public void enterCombat(Person character) {
        // not wholly clear on this either
    }
}