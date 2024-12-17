package com.nicktagliamonte.game;

import com.nicktagliamonte.characters.*;
import com.nicktagliamonte.items.*;
import com.nicktagliamonte.puzzles.MastermindPuzzle;
import com.nicktagliamonte.puzzles.MastermindPuzzleData;
import com.nicktagliamonte.puzzles.SequencePuzzle;
import com.nicktagliamonte.puzzles.SequencePuzzleData;
import com.nicktagliamonte.quests.Objective;
import com.nicktagliamonte.quests.Quest;
import com.nicktagliamonte.rooms.*;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import java.awt.Point;

public class GameState {
    private Region currentRegion;
    private Room currentRoom;
    public Player player;
    public GameEngine gameEngine;
    private List<NPC> currentParty;
    private RegionDialogue currentRegionDialogue;
    private boolean inDialogue = false;
    private String regionFilePath;
    private String adjacencyFilePath;
    private String itemsFilePath;
    private String peopleFilePath;
    private String dialogueFilePath;
    private boolean fromSaveFile;
    public GameTimer gameTimer;
    public String itemContext;
    public safeZoneInventory safeZoneInventory;

    // Constructor
    public GameState(GameEngine gameEngine, String regionFilePath, String adjacencyFilePath, String itemsFilePath,
            String peopleFilePath, String dialogueFilePath, String introFilePath, boolean fromSaveFile) {
        // Initialize the game state, including descriptions and room contents
        this.gameEngine = gameEngine;
        this.currentParty = new ArrayList<>();
        this.regionFilePath = regionFilePath;
        this.adjacencyFilePath = adjacencyFilePath;
        this.itemsFilePath = itemsFilePath;
        this.peopleFilePath = peopleFilePath;
        this.dialogueFilePath = dialogueFilePath;
        this.fromSaveFile = fromSaveFile;
        this.gameTimer = new GameTimer(this);
        this.itemContext = "";
        this.safeZoneInventory = new safeZoneInventory();
        if (!fromSaveFile) {
            //introSequence(introFilePath); TODO: uncomment this
            initializePlayer();
            loadRegion(regionFilePath, adjacencyFilePath, itemsFilePath, peopleFilePath, dialogueFilePath);
        } else {
            //TODO: load from save file
        }
    }

    public void introSequence(String introFilePath) {
        Introduction introduction = new Introduction(introFilePath);
        introduction.display();
    }

    public void initializePlayer() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String name = "";
        while (name.isEmpty()) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Enter a name: ");
            name = scanner.nextLine().trim();
        }
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        CharacterClass characterClass;
        do {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Enter a class (survivalist, technologist, or negotiator): ");
            String classInput = scanner.nextLine();
            characterClass = CharacterClass.createCharacterClass(classInput.trim());
        } while (characterClass == null);

        this.player = new Player(name.trim(), characterClass);
        gameEngine.player = this.player;
    }

    public void loadRegion(String regionFilePath, String adjacenciesFilePath, String itemsFilePath,
            String peopleFilePath, String dialogueFilePath) {
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .excludeFieldsWithoutExposeAnnotation()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();
        try (FileReader regionReader = new FileReader(regionFilePath)) {
            currentRegion = gson.fromJson(regionReader, Region.class);
            for (Room room : currentRegion.getRooms()) {
                if (room.getIsSafe()) {
                    currentRegion.setHasSafeZone(true);
                }
            }
            loadAdjacencyList(adjacenciesFilePath);
            loaditemsInRoom(itemsFilePath);
            loadPeopleInRoom(peopleFilePath);
            loadRegionDialogue(dialogueFilePath);
            initializeCurrentRoom();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAdjacencyList(String adjacenciesFilePath) {
        try (FileReader adjacenciesReader = new FileReader(adjacenciesFilePath)) {
            // Create a Gson instance with custom deserializer
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Adjacency.class, new AdjacencyDeserializer());
            Gson gson = gsonBuilder.create();

            // Define the type for the adjacencies map (a map of room names to a list of
            // adjacencies)
            Type adjacencyMapType = new TypeToken<Map<String, List<Adjacency>>>() {
            }.getType();

            // Parse the file and deserialize into the map of adjacencies
            JsonObject jsonObject = gson.fromJson(adjacenciesReader, JsonObject.class);
            JsonObject adjacencies = jsonObject.getAsJsonObject("adjacencies");
            Map<String, List<Adjacency>> tempAdjacencyMap = gson.fromJson(adjacencies, adjacencyMapType);

            Map<String, Room> roomNameToRoomMap = new HashMap<>();
            for (Room room : currentRegion.getRooms()) {
                roomNameToRoomMap.put(room.getName(), room);
            }

            for (Map.Entry<String, List<Adjacency>> entry : tempAdjacencyMap.entrySet()) {
                String roomName = entry.getKey();
                List<Adjacency> adjacencyList = entry.getValue();

                // Find the actual Room object by name
                Room currentRoom = roomNameToRoomMap.get(roomName);
                if (currentRoom != null) {
                    for (Adjacency adjacency : adjacencyList) {
                        // Now map the adjoiningRoomName to the actual Room object
                        String adjoiningRoomName = adjacency.getAdjoiningRoomName();
                        Room adjoiningRoom = roomNameToRoomMap.get(adjoiningRoomName);
                        if (adjoiningRoom != null) {
                            adjacency.setAdjoiningRoom(adjoiningRoom);
                        }
                    }
                    // Set adjacencies for the current room
                    currentRoom.setAdjacencies(adjacencyList);
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

            for (Room room : currentRegion.getRooms()) {
                if (room.getItemsInRoom() == null) {
                    room.setItemsInRoom(new HashMap<String, Item>());
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

            for (Room room : currentRegion.getRooms()) {
                if (room.getPeopleInRoom() == null) {
                    room.setPeopleInRoom(new HashMap<String, NPC>());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRegionDialogue(String filepath) {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filepath), "UTF-8")) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(RegionDialogue.class, new DialogueDeserializer())
                    .create();

            RegionDialogue regionDialogue = gson.fromJson(JsonParser.parseReader(reader), RegionDialogue.class);
            this.currentRegionDialogue = regionDialogue;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RegionDialogue getCurrentRegionDialogue() {
        return currentRegionDialogue;
    }

    // specifically used for initializing the starting room
    // TODO: when i'm loading save games, i'll probably need to find a way to make
    // it so that the last room which contained the player doesn't conflict with
    // this
    private void initializeCurrentRoom() {
        for (Room room : currentRegion.getRooms()) {
            if (room.hasPlayer()) {
                currentRoom = room;
                currentRoom.updateMapEntry('Y',
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(1)),
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(4)));
            }
        }
    }

    public void updateRegion(String newRegionFilePath, String newAdjacencyFilePath, String newItemsFilePath,
            String newPeopleFilePath, String newDialogueFilePath) {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(newRegionFilePath);
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(newAdjacencyFilePath);
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(newItemsFilePath);
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(newPeopleFilePath);
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(newDialogueFilePath);

        this.regionFilePath = newRegionFilePath;
        this.adjacencyFilePath = newAdjacencyFilePath;
        this.itemsFilePath = newItemsFilePath;
        this.peopleFilePath = newPeopleFilePath;
        this.dialogueFilePath = newDialogueFilePath;

        loadRegion(regionFilePath, adjacencyFilePath, itemsFilePath, peopleFilePath, dialogueFilePath);
    }

    public Region getCurrentRegion() {
        return currentRegion;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public String getRoomDescription(String location) {
        return currentRoom.getDescription();
    }

    public String changeLocation(String directionInput, int distance) {
        Random rand = new Random();
        int roll = rand.nextInt(20) + 1;
        double totalDex = roll + player.getDexterity();

        player.hasKey(currentRoom.getAdjacentRooms());

        try {
            Direction direction = Direction.valueOf(directionInput.toUpperCase());
            String oldLocation = currentRoom.getPlayerPosition();
            currentRoom.updateMapEntry('.', Character.getNumericValue(oldLocation.charAt(1)),
                    Character.getNumericValue(oldLocation.charAt(4)));
            Room newRoom = currentRoom.movePlayer(direction.toString(), distance, totalDex);

            if (newRoom != null && newRoom != currentRoom) {
                // Transition to the new room and update the current location
                currentRoom = newRoom;
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You move " + directionInput.trim() + " " + distance + " steps. You have entered "
                        + newRoom.getName());
                setCurrentRoom(newRoom);
                gameEngine.checkForRandomEvent();
                return null;
            } else if (newRoom != null) {
                // If the player is at the edge, check for adjacency
                String position = currentRoom.getPlayerPosition();
                String[] posArray = position.replace("(", "").replace(")", "").split(", ");
                int posX = Integer.parseInt(posArray[0]);
                int posY = Integer.parseInt(posArray[1]);

                // Check if the player is on an edge and there's an adjacency there
                for (Adjacency adjacency : currentRoom.getAdjacentRooms()) {
                    if (adjacency.getCoordinates().equals("(" + posX + "," + posY + ")")) {
                        // Inform the player about the adjacency and the option to move past it
                        if (adjacency.getType().equals("stairs")) {
                            if (adjacency.getIsStairsUp()) {
                                return "You stand at a " + adjacency.getDescription()
                                        + ". Use ASCEND to take the stairs up.";
                            } else {
                                return "You stand at a " + adjacency.getDescription()
                                        + ". Use DESCEND to take the stairs down.";
                            }
                        } else {
                            return "You stand at a " + adjacency.getDescription()
                                    + ". You can move past the edge to enter " + adjacency.getAdjoiningRoomName();
                        }
                    }
                }
                currentRoom.updateMapEntry('Y',
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(1)),
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(4)));

                return "You move " + directionInput + " " + distance + " steps. You are at " + position;
            } else {
                return "You cannot move further " + directionInput + ". You are at " + currentRoom.getPlayerPosition();
            }
        } catch (IllegalArgumentException e) {
            return "Invalid direction. Valid directions are: NORTH, EAST, SOUTH, WEST, UP, DOWN, LEFT, RIGHT.";
        }
    }

    public String moveToWaypoint(String waypointName) {
        // Check for the waypoint as a series of coordinates
        if (waypointName.charAt(0) == '(') {
            if (waypointName.charAt(3) == ' ') {
                return "If you're entering an ordered pair of coordinates to move to, do not include a space. (x,y) is valid, (x, y) is not.";
            }
            int newX = Character.getNumericValue(waypointName.charAt(1));
            int newY = Character.getNumericValue(waypointName.charAt(3));
            if (isWithinBounds(newX, newY, currentRoom.getMask()) && currentRoom.getMask()[newX][newY] == 1) {
                currentRoom.updateMapEntry('.',
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(1)),
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(4)));
                currentRoom.setPlayerPosition(newX, newY);
                currentRoom.updateMapEntry('Y',
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(1)),
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(4)));
                return "You move to (" + newX + ", " + newY + ").";
            }
        }

        // Check for the waypoint as an NPC
        for (Map.Entry<String, NPC> entry : currentRoom.getPeopleInRoom().entrySet()) {
            if (entry.getValue().getName().equalsIgnoreCase(waypointName)) {
                Point targetPosition = parsePositionString(entry.getKey());
                return moveToAdjacentPosition(targetPosition, "NPC " + waypointName);
            }
        }

        // Check for the waypoint as an exit (adjacent room)
        for (Adjacency adj : currentRoom.getAdjacentRooms()) {
            if (adj.getAdjoiningRoom().getName().equalsIgnoreCase(waypointName)) {
                String newPosition = adj.getCoordinates();
                String[] coordinates = newPosition.replace("(", "").replace(")", "").split(",");
                int newX = Integer.parseInt(coordinates[0]);
                int newY = Integer.parseInt(coordinates[1]);
                currentRoom.updateMapEntry('.',
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(1)),
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(4)));
                currentRoom.setPlayerPosition(newX, newY);
                currentRoom.updateMapEntry('Y',
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(1)),
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(4)));
                if (adj.getType().equals("stairs") && adj.getIsStairsUp()) {
                    return ("You stand at " + adj.getDescription() + ". Use ASCEND to move to " + waypointName);
                } else if (adj.getType().equals("stairs")) {
                    return ("You stand at " + adj.getDescription() + ". Use DESCEND to move to " + waypointName);
                } else {
                    return ("You stand at " + adj.getDescription() + ". Use MOVE or ENTER to move to " + waypointName);
                }
            }
        }

        // Check for the waypoint as an item
        for (Map.Entry<String, Item> entry : currentRoom.getItemsInRoom().entrySet()) {
            if (entry.getValue().getName().equalsIgnoreCase(waypointName)) {
                itemContext = waypointName;
                Point targetPosition = parsePositionString(entry.getKey());
                return moveToAdjacentPosition(targetPosition, "item " + waypointName);
            }
        }

        // Waypoint not found
        return "Waypoint \"" + waypointName
                + "\" not found in this room. Use look for a set of available rooms, where the room name will be enclosed in quotations.";
    }

    private Point parsePositionString(String positionString) {
        positionString = positionString.replaceAll("[()]", ""); // Remove parentheses
        String[] coordinates = positionString.split(",");
        int x = Integer.parseInt(coordinates[0].trim());
        int y = Integer.parseInt(coordinates[1].trim());
        return new Point(x, y);
    }

    private String moveToAdjacentPosition(Point targetPosition, String targetDescription) {
        int[][] walkableMask = currentRoom.getMask();
        int targetX = targetPosition.x;
        int targetY = targetPosition.y;

        // Check adjacent positions (N, E, S, W)
        for (int[] delta : new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }) {
            int newX = targetX + delta[0];
            int newY = targetY + delta[1];
            if (isWithinBounds(newX, newY, walkableMask) && walkableMask[newY][newX] == 1) {
                currentRoom.updateMapEntry('.',
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(1)),
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(4)));
                currentRoom.setPlayerPosition(newX, newY);
                currentRoom.updateMapEntry('Y',
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(1)),
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(4)));
                return "You move next to the " + targetDescription + ". You are at (" + newX + ", " + newY + ").";
            }
        }

        return "No walkable position found near the " + targetDescription + ".";
    }

    private boolean isWithinBounds(int x, int y, int[][] mask) {
        return x >= 0 && x < mask[0].length && y >= 0 && y < mask.length;
    }

    public void enterByCommand(String roomName) {
        Random rand = new Random();
        int roll = rand.nextInt(20) + 1;
        double totalDex = roll + player.getDexterity();

        for (Adjacency adj : currentRoom.getAdjacentRooms()) {
            if (adj.getAdjoiningRoomName().equalsIgnoreCase(roomName) && !adj.getIsLocked()
                    && totalDex >= adj.getdexScore()) {
                setCurrentRoom(adj.getAdjoiningRoom());
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You have entered " + currentRoom.getName());
                return;
            } else if (adj.getAdjoiningRoomName().equalsIgnoreCase(roomName)) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(roomName + " is locked.");
                return;
            } else if (adj.getdexScore() > totalDex) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You fail to reach " + roomName);
                return;
            }
        }
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("no such room was found.  Use LOOK for a list of useable exits, where the connecting room name will be enclosed in quotes.");
    }

    public void ascend(String roomName) {
        Random rand = new Random();
        int roll = rand.nextInt(20) + 1;
        double totalDex = roll + player.getDexterity();

        for (Adjacency adjacency : currentRoom.getAdjacentRooms()) {
            if (adjacency.getType().equals("stairs") && adjacency.getIsStairsUp()
                    && adjacency.getAdjoiningRoom().getName().equalsIgnoreCase(roomName)
                    && adjacency.getdexScore() <= totalDex) {
                setCurrentRoom(adjacency.getAdjoiningRoom());
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You have entered " + currentRoom.getName());
                return;
            } else if (adjacency.getdexScore() > totalDex) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You fail to access " + roomName
                        + " because of a failed dexterity check. The room is too difficult for you to access.");
                return;
            }
        }
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Use ASCEND with the name of a valid adjoining room.  Use LOOK to get a list of adjoining rooms");
    }

    public void descend(String roomName) {
        Random rand = new Random();
        int roll = rand.nextInt(20) + 1;
        double totalDex = roll + player.getDexterity();

        for (Adjacency adjacency : currentRoom.getAdjacentRooms()) {
            if (adjacency.getType().equals("stairs") && !adjacency.getIsStairsUp()
                    && adjacency.getAdjoiningRoom().getName().equalsIgnoreCase(roomName)
                    && adjacency.getdexScore() <= totalDex) {
                setCurrentRoom(adjacency.getAdjoiningRoom());
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You have entered " + currentRoom.getName());
                return;
            } else if (adjacency.getdexScore() > totalDex) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You fail to access " + roomName
                        + " because of a failed dexterity check. The room is too difficult for you to access.");
                return;
            }
        }
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Use DESCEND with the name of a valid adjoining room.  Use LOOK to get a list of adjoining rooms");
    }

    public List<NPC> getCurrentParty() {
        return currentParty;
    }

    public boolean addPartyMember(PartyMember newMember) {
        // Check if party size is already at the maximum
        if (currentParty.size() >= 2) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Party is already full. Cannot add more members.");
            return false;
        }

        // Check if the class is already represented
        for (NPC member : currentParty) {
            if (member instanceof PartyMember
                    && ((PartyMember) member).getClassName().equalsIgnoreCase(newMember.getClassName())) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("A member with this character class is already in the party.");
                return false;
            }
        }
        if (newMember.getClassName().equalsIgnoreCase(player.getClassName())) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Party members cannot have the same class as you.");
            return false;
        }

        // Add the new member and update the player's attributes
        currentParty.add(newMember);
        player.setMaxCarryWeight(player.getMaxCarryWeight() + newMember.getMaxCarryWeight());
        player.increaseRemainingCarryWeight(newMember.getMaxCarryWeight());

        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(newMember.getName() + " has joined the party.");
        return true;
    }

    public void setCurrentRoom(Room room) {
        currentRoom = room;
        currentRoom.triggerTransitionEvent();
        currentRoom.updateMapEntry('Y',
                Character.getNumericValue(currentRoom.getPlayerPosition().charAt(1)),
                Character.getNumericValue(currentRoom.getPlayerPosition().charAt(4)));

        // update people in new room to act as adversary if player alignment is
        // sufficiently low
        Map<String, NPC> people = getCurrentRoom().getPeopleInRoom();
        Iterator<Map.Entry<String, NPC>> iterator = people.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, NPC> entry = iterator.next();
            String currentLocation = entry.getKey();
            NPC currentPerson = entry.getValue();
            if (currentPerson instanceof Neutral
                    && ((Neutral) currentPerson).getMoralityFlag() > player.getAlignment()) {
                people.replace(currentLocation, ((Neutral) currentPerson).actAsAdversary());
                currentRoom.updateMapEntry('A', Character.getNumericValue(currentLocation.charAt(1)),
                        Character.getNumericValue(currentLocation.charAt(3)));

            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public String toSerializableFormat() {
        // this one is going to be somewhat of an undertaking
        return "working";
    }

    public void fromSerializableFormat(String data) {
        // Parse the data and populate the game state
        // Example: Deserialize JSON or parse plain text to restore fields
        // this is a minor nightmare when it comes time.
    }

    public void enterDialogue(Person character) throws FileNotFoundException {
        gameTimer.pause();
        inDialogue = true;

        // Get all dialogues for this character
        Map<String, Dialogue> dialogues = currentRegionDialogue.getDialogue(character.getName());

        if (dialogues == null || !dialogues.containsKey("start")) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(character.getName() + " doesn't want to talk to you.");
            return;
        }

        Dialogue currentDialogue = dialogues.get("start");

        @SuppressWarnings("resource")
        Scanner dialogueScanner = new Scanner(System.in);

        try {
            while (inDialogue) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(currentDialogue.getNpcLine());
                List<DialogueOption> options = currentDialogue.getOptions();

                if (options.isEmpty()) {
                    exitDialogue();
                    break;
                }

                // Filter options based on active quests
                List<DialogueOption> availableOptions = new ArrayList<>();
                List<Quest> activeQuests = player.getActiveQuests(); // Assumes a method returning active quests

                for (DialogueOption option : options) {
                    String nextId = option.getNextDialogueId();
                    if (nextId.startsWith("quest") || nextId.startsWith("objective")) {
                        boolean hasRequiredQuest = activeQuests.stream()
                                .anyMatch(quest -> nextId.contains(quest.getQuestId()));
                        if (hasRequiredQuest) {
                            availableOptions.add(option);
                        }
                    } else {
                        availableOptions.add(option);
                    }
                }

                // Display available options
                if (availableOptions.isEmpty()) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("No dialogue options available.");
                    exitDialogue();
                    break;
                }

                for (int i = 0; i < availableOptions.size(); i++) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println((i + 1) + ": " + availableOptions.get(i).getText());
                }

                int choice = Integer.parseInt(dialogueScanner.nextLine()) - 1;
                DialogueOption selectedOption = availableOptions.get(choice);

                double alignmentDelta = (player.getAlignment() * (selectedOption.getImpact() / 100));
                player.adjustAlignment(alignmentDelta);

                String nextId = selectedOption.getNextDialogueId();

                // Handle quest assignment or completion
                if (nextId.startsWith("assign")) {
                    currentDialogue = dialogues.get(nextId);
                    String questFilePath = currentDialogue.getNpcLine();

                    Quest quest = deserializeQuest(questFilePath);
                    if (quest != null) {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        System.out.println("Do you want to accept quest: " + quest.getTitle() + "? (yes/no)");

                        String playerResponse = dialogueScanner.nextLine().trim().toLowerCase();
                        if (playerResponse.equals("yes")) {
                            player.addQuest(quest);
                            try {
                                Thread.sleep(15);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            System.out.println("You have accepted the quest: " + quest.getTitle()
                                    + ". For more information, access menu > quests.");
                        } else {
                            try {
                                Thread.sleep(15);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            System.out.println("You did not accept this quest. Hopefully, it will still be available later if you want it.");
                        }
                    } else {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        System.out.println("Sorry, there was an error loading the quest.");
                    }

                    exitDialogue();
                    break;
                }

                if (nextId.contains("objective")) {
                    List<Quest> questLog = player.getActiveQuests();
                    for (Quest quest : questLog) {
                        for (Objective objective : quest.getObjectives().values()) {
                            if (!objective.getIsCompleted() && objective.getType().equalsIgnoreCase("dialogue")
                                    && objective.getTarget().equalsIgnoreCase(character.getName())) {
                                quest.completeObjective(objective.getId());
                                break;
                            }
                        }
                    }
                }

                if (nextId.equalsIgnoreCase("exit")) {
                    exitDialogue();
                    break;
                }

                currentDialogue = dialogues.get(nextId);
                if (currentDialogue == null) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Dialogue node missing for ID: " + nextId);
                    exitDialogue();
                    break;
                }
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException f) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Invalid choice. Please try again.");
        }
    }

    private Quest deserializeQuest(String questFilePath) throws FileNotFoundException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemDeserializer())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        FileReader reader = new FileReader(questFilePath);
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        Quest quest = gson.fromJson(jsonObject, Quest.class);
        quest.setGameState(this);
        return quest;
    }

    public void exitDialogue() {
        gameTimer.resume();
        inDialogue = false;
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("The conversation is over.");
    }

    public void enterBarter(Person npc) {
        gameTimer.pause();
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Entering barter mode with " + npc.getName());
        boolean hasNegotiator = player.getCharacterClass().getClassName().equalsIgnoreCase("negotiator");
        for (NPC member : currentParty) {
            PartyMember partyMember = (PartyMember) member;
            if (partyMember.getCharacterClass().getClassName().equalsIgnoreCase("negotiator")) {
                hasNegotiator = true;
            }
        }

        Barter barter = new Barter(player, player.getInventory(), npc.getInventory(), 0.0, hasNegotiator);

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        boolean inBarter = true;

        while (inBarter) {
            barter.displayInventory();
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("\nYour Purchase Power: " + barter.getPurchasePower());
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("1. Offer items");
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("2. Select an item to purchase");
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("3. Exit barter");
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    // Logic to let the player offer items
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Enter items to offer (comma-separated names):");
                    String offerInput = scanner.nextLine();
                    List<Item> offeredItems = parseItems(offerInput, player.getInventory());
                    barter.playerOffersItems(offeredItems);
                    break;
                case 2:
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Enter item name to purchase:");
                    String itemName = scanner.nextLine();
                    Item itemSelected = null;
                    for (Item item : npc.getInventory()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemSelected = item;
                        }
                    }
                    if (itemSelected != null) {
                        barter.playerSelectsItem(itemSelected);
                    } else {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        System.out.println("Item not found.");
                    }
                    break;
                case 3:
                    gameTimer.resume();
                    inBarter = false;
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Exiting Barter.");
                    break;
                default:
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Invalid choice.");
            }
        }
    }

    private List<Item> parseItems(String input, List<Item> inventory) {
        List<Item> items = new ArrayList<>();
        String[] itemNames = input.split(",");
        for (String itemName : itemNames) {
            itemName = itemName.trim();
            for (Item item : inventory) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    items.add(item);
                }
            }
        }
        return items;
    }

    public void enterCombat(Person character) {
        gameTimer.pause();
        player.setIsHiding(false);
        List<Person> combatants = new ArrayList<>();
        combatants.add(player); // Add player
        String location = "";

        Map<String, NPC> people = getCurrentRoom().getPeopleInRoom();
        Iterator<Map.Entry<String, NPC>> iterator = people.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, NPC> entry = iterator.next();
            if (entry.getValue().getName().equals(character.getName())) {
                location = entry.getKey();
                break;
            }
        }

        if (character instanceof Neutral) {
            Neutral neutralToFight = (Neutral) character;
            people.put(location, neutralToFight.actAsAdversary());
            currentRoom.updateMapEntry('A', Character.getNumericValue(location.charAt(1)),
                    Character.getNumericValue(location.charAt(3)));
            combatants.add(neutralToFight.actAsAdversary());
        } else if (character instanceof Adversary) {
            for (String allyName : ((Adversary) character).getAllies()) {
                for (NPC person : getCurrentRoom().getPeopleInRoom().values()) {
                    if (person.getName().equalsIgnoreCase(allyName)) {
                        combatants.add(person);
                    }
                }
            }
            combatants.add(character);
        }

        // Add party members if any
        if (!currentParty.isEmpty()) {
            combatants.addAll(currentParty);
        }

        // Roll initiative
        for (Person combatant : combatants) {
            int initiative = 0;
            if (combatant instanceof Player) {
                initiative = rollD20() + (int) player.getDexterity();
                if (player.inHunger()) {
                    initiative -= 2;
                }
            } else if (combatant instanceof NPC) {
                NPC npcCombatant = (NPC) combatant;
                initiative = rollD20() + (int) npcCombatant.getDexterity();
            }
            combatant.setInitiative(initiative);
        }

        // Sort by initiative (highest first)
        combatants.sort((c1, c2) -> Integer.compare(c2.getInitiative(), c1.getInitiative()));

        @SuppressWarnings("unused")
        Combat combat = new Combat(combatants, this, location);
        if (!player.isAlive()) {
            playerDead();
        }
        gameTimer.resume();
    }

    public int rollD20() {
        return (int) (Math.random() * 20) + 1;
    }

    public void playerDead() {
        if (fromSaveFile) {
            // TODO: handle this when i handle persistence
        } else {
            // it's harsh, but reinitialize the gamestate with the data that it had when the
            // game started
            loadRegion(regionFilePath, adjacencyFilePath, itemsFilePath, peopleFilePath, dialogueFilePath);
            this.currentParty = new ArrayList<>();
        }
    }

    @SuppressWarnings("resource")
    public void startLockpickingSequence(String roomName, Adjacency adjacency) {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("You are attempting to pick the lock of " + roomName);

        double intelligenceBonus = player.getIntelligence() / 2;
        int baseChances = adjacency.getBaseChances() + (int) Math.floor(intelligenceBonus);
        boolean success = false;

        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("The lock feels intricate. You have " + baseChances + " attempts.");
        for (int attempt = 1; attempt <= baseChances; attempt++) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Attempt " + attempt + ": What will you do?");
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Enter a number between 1 and " + adjacency.getDifficulty() + ":");
            int playerInput = new Scanner(System.in).nextInt();
            int lockValue = (int) (Math.random() * adjacency.getDifficulty()) + 1;

            if (playerInput == lockValue) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You hear a satisfying click! The lock opens.");
                player.gainXP(20, this);
                success = true;
                break;
            } else {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("The lock resists. Keep trying...");
            }
        }

        if (!success) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You failed to pick the lock. Maybe try again later.");
        } else {
            unlockRoom(roomName);
        }
    }

    private void unlockRoom(String roomName) {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(roomName + " is now unlocked!");
        for (Adjacency adj : currentRoom.getAdjacentRooms()) {
            if (adj.getAdjoiningRoomName().equalsIgnoreCase(roomName)) {
                adj.setIsLocked(false);
            }
        }
    }

    @SuppressWarnings("resource")
    public void enterCombinationLockSequence(String roomName, Adjacency adj) {
        
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("You are attempting to unlock " + roomName);
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Enter the combination for the room:");

        int playerInput = new Scanner(System.in).nextInt();

        if (playerInput == adj.getCombination()) {
            adj.setIsLocked(false);
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You hear a satisfying click.  The lock opens.");

            for (Quest quest : player.getActiveQuests()) {
                for (Objective objective : quest.getObjectives().values()) {
                    if (objective.getType().equalsIgnoreCase("puzzle")
                            && objective.getTarget().equalsIgnoreCase(roomName)) {
                        quest.completeObjective(objective.getId());
                        break;
                    }
                }
            }
        } else {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("That combination was incorrect.");
        }
    }

    public void launchSequencePuzzle(String puzzleFilePath) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemDeserializer())
                .create();
        try {
            FileReader reader = new FileReader(puzzleFilePath);
            SequencePuzzleData data = gson.fromJson(reader, SequencePuzzleData.class);
            SequencePuzzle currentPuzzle = new SequencePuzzle(data, this);
            currentPuzzle.startPuzzleLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchMastermindPuzzle(String puzzleFilePath) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemDeserializer())
                .create();
        try {
            FileReader reader = new FileReader(puzzleFilePath);
            MastermindPuzzleData data = gson.fromJson(reader, MastermindPuzzleData.class);
            MastermindPuzzle currentPuzzle = new MastermindPuzzle(data, this);
            currentPuzzle.startPuzzleLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}