package com.nicktagliamonte.game;

import com.nicktagliamonte.Spells.PlasmaBolt;
import com.nicktagliamonte.Spells.Spell;
import com.nicktagliamonte.Spells.SpellDeserializer;
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
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.io.File;
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
    @Expose private Region currentRegion;
    @Expose private Room currentRoom;
    @Expose public Player player;
    @Expose public transient GameEngine gameEngine;
    @Expose private List<NPC> currentParty;
    @Expose private transient RegionDialogue currentRegionDialogue;
    @Expose private boolean inDialogue = false;
    @Expose private transient String regionFilePath;
    @Expose private transient String adjacencyFilePath;
    @Expose private transient String itemsFilePath;
    @Expose private transient String peopleFilePath;
    @Expose private transient String dialogueFilePath;
    @Expose private transient boolean fromSaveFile;
    @Expose public transient GameTimer gameTimer;
    @Expose public transient String itemContext;
    @Expose public safeZoneInventory safeZoneInventory;
    @Expose public Load load;

    public GameState(GameEngine gameEngine, String regionFilePath, String adjacencyFilePath, String itemsFilePath,
            String peopleFilePath, String dialogueFilePath, String introFilePath, boolean fromSaveFile) {
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
        //introSequence(introFilePath);
        initializePlayer();
        loadRegion(regionFilePath, adjacencyFilePath, itemsFilePath, peopleFilePath, dialogueFilePath);
    }

    public GameState(GameEngine gameEngine, Load load) {
        this.load = load;
        this.gameEngine = gameEngine;
        this.player = gameEngine.getPlayer();
        this.inDialogue = load.inDialogue;
        this.itemContext = "";
        this.fromSaveFile = true;
        this.gameTimer = gameEngine.getGameTimer();
        this.currentRegion = load.region;
        this.safeZoneInventory = new safeZoneInventory();
        this.currentParty = load.currentParty;
        safeZoneInventory.setInventory(load.safeZoneInventory.getInventory());
        initializeCurrentRoomFromSave(load);
        loadRegionDialogueFromSave();
        setTrapFields();
    }

    public void introSequence(String introFilePath) {
        Introduction introduction = new Introduction(introFilePath);
        introduction.display();
    }

    public void initializePlayer() {
        Scanner scanner = new Scanner(System.in);
        
        // Get the player's name
        String name = getPlayerName(scanner);
        
        // Get the player's character class
        CharacterClass characterClass = getCharacterClass(scanner);
    
        this.player = new Player(name, characterClass);
    
        // Special setup for Technologist class
        if (player.getCharacterClass().getClassName().equalsIgnoreCase("technologist")) {
            player.addSpell(new PlasmaBolt());
            System.out.println("As you get up, you feel a weight in your pack.");
            System.out.println("Some metallic device is in there, with a trigger.");
            System.out.println("You can use this device to cast Plasma Bolt in combat.");
        }
    
        gameEngine.player = this.player;
    }
    
    private String getPlayerName(Scanner scanner) {
        String name = "";
        while (name.isEmpty()) {
            sleep();
            System.out.println("Enter a name: ");
            name = scanner.nextLine().trim();
        }
        // Capitalize the first letter and ensure the rest is lowercase
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
    
    private CharacterClass getCharacterClass(Scanner scanner) {
        CharacterClass characterClass;
        do {
            sleep();
            System.out.println("Enter a class (survivalist, technologist, or negotiator): ");
            String classInput = scanner.nextLine().trim();
            characterClass = CharacterClass.createCharacterClass(classInput);
            
            if (characterClass == null) {
                System.out.println("Invalid class! Please choose from: survivalist, technologist, or negotiator.");
            }
        } while (characterClass == null);
        
        return characterClass;
    }
    
    private void sleep() {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
            currentRegion.transition();
            for (Room room : currentRegion.getRooms()) {
                if (room.getIsSafe()) {
                    currentRegion.setHasSafeZone(true);
                }
            }
            loadAdjacencyList(adjacenciesFilePath);
            loadItemsInRoom(itemsFilePath);
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

    private void loadItemsInRoom(String itemsFilePath) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<Item>>() {}.getType(), new ItemDeserializer())
                .create();
    
        try {
            sleep(); // Replace Thread.sleep(15) with sleep()
            try (FileReader itemsReader = new FileReader(itemsFilePath)) {
                // Parse the JSON structure for rooms and their coordinates with item details
                JsonObject jsonObject = gson.fromJson(itemsReader, JsonObject.class);
                JsonObject itemsJson = jsonObject.getAsJsonObject("items");
    
                Map<String, Map<String, List<Item>>> tempItemsMap = new HashMap<>();
    
                // Iterate over each room and parse item data
                for (Map.Entry<String, JsonElement> roomEntry : itemsJson.entrySet()) {
                    String roomName = roomEntry.getKey();
                    JsonObject roomItemsJson = roomEntry.getValue().getAsJsonObject();
                    Map<String, List<Item>> itemsMap = new HashMap<>();
    
                    // Iterate over each coordinate and parse item info
                    for (Map.Entry<String, JsonElement> coordinateEntry : roomItemsJson.entrySet()) {
                        String coordinates = coordinateEntry.getKey();
                        JsonObject itemJson = coordinateEntry.getValue().getAsJsonObject();
    
                        // Deserialize the item JSON into a List<Item> using the custom deserializer
                        List<Item> items = gson.fromJson(itemJson, new TypeToken<List<Item>>() {}.getType());
    
                        // Add the list of items to the map for the current coordinate
                        itemsMap.put(coordinates, items);
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
                for (Map.Entry<String, Map<String, List<Item>>> roomEntry : tempItemsMap.entrySet()) {
                    String roomName = roomEntry.getKey();
                    Map<String, List<Item>> itemsInRoom = roomEntry.getValue();
    
                    // Find the actual Room object by name
                    Room currentRoom = roomNameToRoomMap.get(roomName);
    
                    if (currentRoom != null) {
                        currentRoom.setItemsInRoom(itemsInRoom); // Set items for this room
                    }
                }
    
                // Ensure that every room has its items initialized, even if empty
                for (Room room : currentRegion.getRooms()) {
                    if (room.getItemsInRoom() == null) {
                        room.setItemsInRoom(new HashMap<>());
                    }
                }
    
                sleep(); // Replace Thread.sleep(15) with sleep()
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
    
            // Ensure every room has an initialized people map
            for (Room room : currentRegion.getRooms()) {
                if (room.getPeopleInRoom() == null) {
                    room.setPeopleInRoom(new HashMap<>());
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
    
            sleep(); // Optional: Adding a sleep after loading dialogue if necessary
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: update these paths with the paths to the real game data when it is written
    public void loadRegionDialogueFromSave() {
        // Get the base directory of the game
        String baseDir = System.getProperty("user.dir");

        // Construct relative paths to dialogue files
        String dialoguePath1 = baseDir + File.separator + "app" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "resources" 
                                + File.separator + "json" + File.separator + "dialogue" + File.separator + "test_dialogue.json";
        String dialoguePath2 = baseDir + File.separator + "app" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "resources" 
                                + File.separator + "json" + File.separator + "dialogue" + File.separator + "test_dialogue_2.json";

        // Load dialogue based on region
        String currentRegionName = currentRegion.getRegionName();
        if (currentRegionName.equalsIgnoreCase("The Dilapidated Building")) {
            loadRegionDialogue(dialoguePath1);
        } else {
            loadRegionDialogue(dialoguePath2);
        }
    }

    public RegionDialogue getCurrentRegionDialogue() {
        return currentRegionDialogue;
    }

    // specifically used for initializing the starting room
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

    private void initializeCurrentRoomFromSave(Load load) {
        currentRoom = load.currentRoom;
        currentRoom.updateMapEntry('Y',
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(1)),
                        Character.getNumericValue(currentRoom.getPlayerPosition().charAt(4)));
    }

    private void setTrapFields() {
        for (Room room : currentRegion.getRooms()) {
            for (Object items : room.getItemsInRoom().values()) {
                // Check if 'items' is an instance of List (or any iterable collection)
                if (items instanceof Iterable<?>) {
                    // Cast it to Iterable and iterate over its elements
                    for (Object obj : (Iterable<?>) items) {
                        if (obj instanceof Item) {
                            Item item = (Item) obj;
                            if (item instanceof Trap) {
                                ((Trap) item).setGameState(this);
                            }
                        }
                    }
                }
            }        

            for (NPC npc : room.getPeopleInRoom().values()) {
                for (Item item : npc.getInventory()) {
                    if (item instanceof Trap) {
                        ((Trap) item).setGameState(this);
                    }
                }
            }
        }

        for (Item item : player.getInventory()) {
            if (item instanceof Trap) {
                ((Trap) item).setGameState(this);
            }
        }

        for (Item item : safeZoneInventory.getInventory()) {
            if (item instanceof Trap) {
                ((Trap) item).setGameState(this);
            }
        }

        for (Quest quest : player.getQuestLog()) {
            quest.setGameState(this);
        }
    }

    public void updateRegion(String newRegionFilePath, String newAdjacencyFilePath, String newItemsFilePath,
            String newPeopleFilePath, String newDialogueFilePath) {
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
                int newX = Integer.parseInt(coordinates[0].trim());
                int newY = Integer.parseInt(coordinates[1].trim());
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
        for (Map.Entry<String, List<Item>> entry : currentRoom.getItemsInRoom().entrySet()) {
            for (Item item : entry.getValue()) {
                if (item.getName().equalsIgnoreCase(waypointName)) {
                    itemContext = waypointName;
                    Point targetPosition = parsePositionString(entry.getKey());
                    return moveToAdjacentPosition(targetPosition, "item " + waypointName);
                }
            }
        }
    
        // Waypoint not found
        return "Waypoint \"" + waypointName
                + "\" not found in this room. Use look for a set of available waypoints, which take the form of items and people.";
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
        for (int[] delta : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
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
                System.out.println("You have entered " + adj.getAdjoiningRoomName());
                return;
            } else if (adj.getAdjoiningRoomName().equalsIgnoreCase(roomName)) {
                sleep();  // Use the sleep helper
                System.out.println(roomName + " is locked.");
                return;
            } else if (adj.getdexScore() > totalDex) {
                sleep();  // Use the sleep helper
                System.out.println("You fail to reach " + roomName);
                return;
            }
        }
        sleep();  // Use the sleep helper
        System.out.println("No such room was found. Use LOOK for a list of usable exits, where the connecting room name will be enclosed in quotes.");
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
                sleep();  // Use the sleep helper
                System.out.println("You have entered " + currentRoom.getName());
                return;
            } else if (adjacency.getdexScore() > totalDex) {
                sleep();  // Use the sleep helper
                System.out.println("You fail to access " + roomName
                        + " because of a failed dexterity check. The room is too difficult for you to access.");
                return;
            }
        }
        sleep();  // Use the sleep helper
        System.out.println("Use ASCEND with the name of a valid adjoining room. Use LOOK to get a list of adjoining rooms.");
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

    public void removePartyMember(NPC personToRemove) {
        currentParty.remove(personToRemove);
    }

    public void setCurrentRoom(Room room) {
        if (!room.isAccessible()) {
            System.out.println(room.getDenialMessage());
            return;
        }

        currentRoom = room;
        currentRoom.triggerTransitionEvent();
        currentRoom.updateMapEntry('Y',
                Character.getNumericValue(currentRoom.getPlayerPosition().charAt(1)),
                Character.getNumericValue(currentRoom.getPlayerPosition().charAt(4)));

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

    public void enterDialogue(NPC character) throws FileNotFoundException {
        gameTimer.pause();
        inDialogue = true;
    
        // Get all dialogues for this character
        Map<String, Dialogue> dialogues = currentRegionDialogue.getDialogue(character.getName());
    
        if (dialogues == null || !dialogues.containsKey("start_1")) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(character.getName() + " doesn't want to talk to you.");
            return;
        }        
    
        // Check if all dialogue nodes are exhausted and print a message if necessary
        if (!dialogues.containsKey(character.getStartNode())) {
            System.out.println(character.getName() + " has nothing more to say to you. They will be leaving soon.");
            return;
        }
    
        Dialogue currentDialogue = dialogues.get(character.getStartNode());
    
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
                List<Quest> activeQuests = player.getActiveQuests();
    
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
                    String questFileName = currentDialogue.getNpcLine();
                    String questFilePath = "";
                    if (questFileName != null && !questFileName.isEmpty()) {
                        questFilePath = System.getProperty("user.dir") 
                                        + File.separator + "app" + File.separator + "src" + File.separator + "main" 
                                        + File.separator + "java" + File.separator + "resources" 
                                        + File.separator + "json" + File.separator + "quests" 
                                        + File.separator + questFileName;
                    } else {
                        System.out.println("No quest file name provided by NPC dialogue.");
                    }
    
                    Quest quest = null;
                    if (!(questFilePath.equals(""))) {
                        quest = deserializeQuest(questFilePath);
                    }
                    if (quest != null) {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        String playerResponse;

                        do {
                            System.out.println("Do you want to accept quest: " + quest.getTitle() + "? (y/n)");
                            playerResponse = dialogueScanner.nextLine().trim().toLowerCase();

                            if (!playerResponse.equals("y") && !playerResponse.equals("n")) {
                                System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
                            }
                        } while (!playerResponse.equals("y") && !playerResponse.equals("n"));

                        if (playerResponse.equals("y")) {
                            player.addQuest(quest);
                            setRoomAccessibility(quest);
                            incrementStartNode(character);  // Increment startNode on quest acceptance
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
                                incrementStartNode(character);  // Increment startNode on objective completion
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

    public void incrementStartNode(NPC character) {
        String currentStartNode = character.getStartNode();
        String[] parts = currentStartNode.split("_");
        if (parts.length == 2) {
            int nextNodeNumber = Integer.parseInt(parts[1]) + 1;
            character.setStartNode("start_" + nextNodeNumber);
        }
    }

    public void incrementStartNode(String questGiverName) {
        NPC character = null;
        for (Room room : currentRegion.getRooms()) {
            for (NPC npc : room.getPeopleInRoom().values()) {
                if (npc.getName().equalsIgnoreCase(questGiverName)) {
                    character = npc;
                    break;
                }
            }
        }

        if (character == null) {
            return;
        }

        String currentStartNode = character.getStartNode();
        String[] parts = currentStartNode.split("_");
        if (parts.length == 2) {
            int nextNodeNumber = Integer.parseInt(parts[1]) + 1;
            character.setStartNode("start_" + nextNodeNumber);
        }
    }

    private Quest deserializeQuest(String questFilePath) throws FileNotFoundException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemDeserializer())
                .registerTypeAdapter(Spell.class, new SpellDeserializer())
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

    @SuppressWarnings("resource")
    public void enterBarter(Person npc) {
        gameTimer.pause();
        sleep();
        System.out.println("Entering barter mode with " + npc.getName());
    
        boolean hasNegotiator = player.getCharacterClass().getClassName().equalsIgnoreCase("negotiator");
        
        // Check if any party member is a negotiator
        for (NPC member : currentParty) {
            PartyMember partyMember = (PartyMember) member;
            if (partyMember.getCharacterClass().getClassName().equalsIgnoreCase("negotiator")) {
                hasNegotiator = true;
                break;  // No need to continue if a negotiator is found
            }
        }
    
        Barter barter = new Barter(player, player.getInventory(), npc.getInventory(), 0.0, hasNegotiator);
        
        Scanner scanner = new Scanner(System.in);  // Using try-with-resources to automatically close the scanner
        boolean inBarter = true;

        while (inBarter) {
            barter.displayInventory();
            System.out.println("\nYour Purchase Power: " + barter.getPurchasePower());
            System.out.println("1. Offer items");
            System.out.println("2. Select an item to purchase");
            System.out.println("3. Exit barter");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    sleep();  // Replaced repetitive sleep calls with a helper method
                    System.out.println("Enter items to offer (comma-separated names):");
                    String offerInput = scanner.nextLine();
                    List<Item> offeredItems = parseItems(offerInput, player.getInventory());
                    barter.playerOffersItems(offeredItems);
                    break;
                case 2:
                    sleep();  // Replaced repetitive sleep calls with a helper method
                    System.out.println("Enter item name to purchase:");
                    String itemName = scanner.nextLine();
                    Item itemSelected = null;
                    for (Item item : npc.getInventory()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemSelected = item;
                            break;  // Stop once the item is found
                        }
                    }
                    if (itemSelected != null) {
                        barter.playerSelectsItem(itemSelected);
                    } else {
                        sleep();  // Replaced repetitive sleep calls with a helper method
                        System.out.println("Item not found.");
                    }
                    break;
                case 3:
                    gameTimer.resume();
                    inBarter = false;
                    sleep();  // Replaced repetitive sleep calls with a helper method
                    System.out.println("Exiting Barter.");
                    break;
                default:
                    sleep();  // Replaced repetitive sleep calls with a helper method
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
            for (NPC partyMember : currentParty) {
                if (!(partyMember instanceof Conveyance)) {
                    combatants.add(partyMember);
                }
            }
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
            this.player = load.player;
            this.inDialogue = load.inDialogue;
            this.itemContext = "";
            this.currentRegion = load.region;
            this.currentParty = load.currentParty;
            safeZoneInventory.setInventory(load.safeZoneInventory.getInventory());
            initializeCurrentRoomFromSave(load);
            loadRegionDialogueFromSave();
            setTrapFields();
        } else {
            // it's harsh, but reinitialize the gamestate with the data that it had when the
            // game started
            loadRegion(regionFilePath, adjacencyFilePath, itemsFilePath, peopleFilePath, dialogueFilePath);
            this.currentParty = new ArrayList<>();
        }
    }

    @SuppressWarnings("resource")
    public void startLockpickingSequence(String roomName, Adjacency adjacency) {
        sleep();  // Replaced Thread.sleep(15) with sleep() helper method
        System.out.println("You are attempting to pick the lock of " + roomName);

        double intelligenceBonus = player.getIntelligence() / 2;
        int baseChances = adjacency.getBaseChances() + (int) Math.floor(intelligenceBonus);
        boolean success = false;

        sleep();  // Replaced Thread.sleep(15) with sleep() helper method
        System.out.println("The lock feels intricate. You have " + baseChances + " attempts.");
        for (int attempt = 1; attempt <= baseChances; attempt++) {
            sleep();  // Replaced Thread.sleep(15) with sleep() helper method
            System.out.println("Attempt " + attempt + ": What will you do?");
            
            sleep();  // Replaced Thread.sleep(15) with sleep() helper method
            System.out.println("Enter a number between 1 and " + adjacency.getDifficulty() + ":");
            int playerInput = new Scanner(System.in).nextInt();
            int lockValue = (int) (Math.random() * adjacency.getDifficulty()) + 1;

            if (playerInput == lockValue) {
                sleep();  // Replaced Thread.sleep(15) with sleep() helper method
                System.out.println("You hear a satisfying click! The lock opens.");
                player.gainXP(20, this);
                success = true;
                break;
            } else if (playerInput < lockValue) {
                System.out.println("The lock resists. You might have better luck with higher numbers.");
            } else {
                sleep();  // Replaced Thread.sleep(15) with sleep() helper method
                System.out.println("Try a lower number.");
            }
        }

        if (!success) {
            sleep();  // Replaced Thread.sleep(15) with sleep() helper method
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
        sleep();  // Replaced Thread.sleep(15) with sleep() helper method
        System.out.println("You are attempting to unlock " + roomName);

        sleep();  // Replaced Thread.sleep(15) with sleep() helper method
        System.out.println("Enter the combination for the room:");

        int playerInput = new Scanner(System.in).nextInt();

        if (playerInput == adj.getCombination()) {
            adj.setIsLocked(false);
            sleep();  // Replaced Thread.sleep(15) with sleep() helper method
            System.out.println("You hear a satisfying click. The lock opens.");

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
            sleep();  // Replaced Thread.sleep(15) with sleep() helper method
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

    public void setRoomAccessibility(Quest quest) {
        for (Objective objective : quest.getObjectives().values()) {
            if (objective.getType().equalsIgnoreCase("movement")) {
                for (Room room : currentRegion.getRooms()) {
                    if (room.getName().equalsIgnoreCase(objective.getTarget())) {
                        room.setAccessible(true);
                    }
                }
            }
        }
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .setPrettyPrinting()
                        .create();

        JsonObject gameStateJson = new JsonObject();

        // Serialize basic fields
        gameStateJson.addProperty("inDialogue", inDialogue);

        // Serialize `currentRegion`
        if (currentRegion != null) {
            gameStateJson.add("currentRegion", gson.fromJson(currentRegion.toSerializableFormat(), JsonObject.class));
        }

        // Serialize `currentRoom` by name
        if (currentRoom != null) {
            gameStateJson.addProperty("currentRoom", currentRoom.getName());
        }

        // Serialize `player`
        if (player != null) {
            gameStateJson.add("player", gson.fromJson(player.toSerializableFormat(), JsonObject.class));
        }

        // Serialize `currentParty`
        if (currentParty != null) {
            JsonObject partyJson = new JsonObject();
            for (int i = 0; i < currentParty.size(); i++) {
                partyJson.add("NPC_" + i, gson.fromJson(((PartyMember) currentParty.get(i)).toSerializableFormat(), JsonObject.class));
            }
            gameStateJson.add("currentParty", partyJson);
        }

        // Serialize `safeZoneInventory`
        if (safeZoneInventory != null) {
            gameStateJson.add("safeZoneInventory", gson.fromJson(safeZoneInventory.toSerializableFormat(), JsonObject.class));
        }

        return gson.toJson(gameStateJson);
    }

    public void credits() {
        @SuppressWarnings("unused")
        Credits credits = new Credits();
        System.exit(0);
    }
}