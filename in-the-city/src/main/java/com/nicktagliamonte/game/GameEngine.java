package com.nicktagliamonte.game;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.*;
import com.nicktagliamonte.quests.Objective;
import com.nicktagliamonte.quests.Quest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GameEngine {
    @Expose public transient Player player;
    @Expose public transient boolean isInMenu;
    @Expose private transient Menu menu;
    @Expose private GameState gameState;
    @Expose private transient GameTimer timer;
    @Expose private int turnsInStealth;
    
    @Expose private transient Scanner scanner = new Scanner(System.in);

    // Constructor for a new game
    public GameEngine() {
        // Get the base directory
        String baseDir = System.getProperty("user.dir");
    
        // Construct the file paths relative to the base directory
        String regionsPath = baseDir + File.separator + "src" + File.separator + "main" + File.separator + "java" 
                            + File.separator + "resources" + File.separator + "json" + File.separator + "regions"
                            + File.separator + "test_region.json";
        
        String adjacenciesPath = baseDir + File.separator + "src" + File.separator + "main" + File.separator + "java" 
                                + File.separator + "resources" + File.separator + "json" + File.separator + "adjacencies"
                                + File.separator + "test_adjacencies.json";
    
        String itemsPath = baseDir + File.separator + "src" + File.separator + "main" + File.separator + "java" 
                           + File.separator + "resources" + File.separator + "json" + File.separator + "items"
                           + File.separator + "test_items.json";
    
        String peoplePath = baseDir + File.separator + "src" + File.separator + "main" + File.separator + "java" 
                            + File.separator + "resources" + File.separator + "json" + File.separator + "people"
                            + File.separator + "test_people.json";
    
        String dialoguePath = baseDir + File.separator + "src" + File.separator + "main" + File.separator + "java" 
                              + File.separator + "resources" + File.separator + "json" + File.separator + "dialogue"
                              + File.separator + "test_dialogue.json";
    
        String introductionPath = baseDir + File.separator + "src" + File.separator + "main" + File.separator + "java" 
                                  + File.separator + "resources" + File.separator + "json" + File.separator + "introduction"
                                  + File.separator + "introduction.json";
        
        // Initialize the GameState with the constructed paths
        this.menu = new Menu(this);
        this.gameState = new GameState(this, regionsPath, adjacenciesPath, itemsPath, peoplePath, dialoguePath, introductionPath, false);
        this.timer = new GameTimer(gameState);
        this.turnsInStealth = 0;
        this.isInMenu = false;
    }    

    public GameEngine(Load load) {
        this.isInMenu = false;
        this.menu = new Menu(this);

        this.turnsInStealth = load.turnsInStealth;
        this.player = load.player;

        this.gameState = new GameState(this, load);
        this.timer = new GameTimer(gameState);
    }

    public void startGame(boolean fromSaveFile, String saveFilePath) {    
        boolean isRunning = true;
        int invalidCommands = 0;
    
        while (isRunning) {
            if (isInMenu) {
                menu.displayMenu();
            } else {
                // Timer checks
                if (timer.getElapsedTime() >= 2000) {
                    timer.checkForEvent();
                }
                timer.checkForHunger();
                timer.checkForThirst();
                for (NPC npc : gameState.getCurrentParty()) {
                    if (npc instanceof Conveyance) {
                        timer.checkForConveyanceHunger();
                    }
                }
    
                System.out.print("\nEnter command: ");
                String input = scanner.nextLine().trim();
                String[] splitInput = input.split(" ", 2);
                String commandName = splitInput[0].toLowerCase();
                String[] args = (splitInput.length > 1) ? new String[]{splitInput[1]} : new String[0];
    
                // Clear item context if not applicable
                if (!gameState.itemContext.equals("") && 
                    !(commandName.equalsIgnoreCase("Examine") || commandName.equalsIgnoreCase("Use") || 
                    commandName.equalsIgnoreCase("Equip") || commandName.equalsIgnoreCase("Take"))) {
                    gameState.itemContext = "";
                }
    
                // Process command
                GameCommand command = GameCommand.fromString(commandName);
    
                if (command != null) {
                    command.execute(args, gameState);
                    invalidCommands = 0;
                } else {
                    System.out.println("Unknown command.");
                    invalidCommands++;
    
                    if (invalidCommands >= 3) {
                        System.out.println("Type 'menu' to navigate to a list of valid commands.");
                        invalidCommands = 0;
                    }
                }
    
                // Quest objective completion check
                List<Quest> activeQuests = gameState.getPlayer().getActiveQuests();
                for (Quest quest : activeQuests) {
                    for (Objective objective : quest.getObjectives().values()) {
                        if (!objective.getIsCompleted()) {
                            String objectiveType = objective.getType();
                            String target = objective.getTarget();
                            
                            // Handle different types of objectives
                            switch (objectiveType.toLowerCase()) {
                                case "item":
                                    if (gameState.getPlayer().getInventory().stream()
                                        .anyMatch(item -> item.getName().equalsIgnoreCase(target))) {
                                        quest.completeObjective(objective.getId());
                                    }
                                    break;
                                case "movement":
                                    if (gameState.getCurrentRoom().getName().equalsIgnoreCase(target)) {
                                        quest.completeObjective(objective.getId());
                                    }
                                    break;
                                case "stealth":
                                    int turnsNeeded = Integer.parseInt(target);
                                    if (gameState.getPlayer().getIsHiding()) {
                                        turnsInStealth++;
                                        if (turnsInStealth == turnsNeeded) {
                                            quest.completeObjective(objective.getId());
                                        }
                                    } else {
                                        turnsInStealth = 0;  // Reset if not hiding
                                    }
                                    break;
                            }
                        }
                    }
                }
    
                // Exit condition
                if (commandName.equals("quit")) {
                    isRunning = false;
                }
            }
        }
        scanner.close();
        System.out.println("Thanks for playing!");
    }



    @SuppressWarnings("resource")
    public int getPlayerInputAsInt() {
        Scanner scanner = new Scanner(System.in);
        int input = -1; // Initialize with an invalid value to enter the loop
        
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                input = Integer.parseInt(scanner.nextLine().trim()); // Parse input as an integer
                return input; // Return the input if it's a valid integer
            } catch (NumberFormatException e) {
                // Handle non-integer input
                System.out.println("Please select one of the menu options by entering its number.");
            }
        }
    }    

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Player getPlayer() {
        return player;
    }

    public GameTimer getGameTimer() {
        return timer;
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .setPrettyPrinting()
                        .create();

        JsonObject gameEngineJson = new JsonObject();

        gameEngineJson.addProperty("turnsInStealth", turnsInStealth);

        if (gameState != null) {
            gameEngineJson.add("gameState", gson.fromJson(gameState.toSerializableFormat(), JsonObject.class));
        }

        return gson.toJson(gameEngineJson);
    }

     // Save the game to a file
    public void saveGameToFile(String filePath) {
        String serializedGame = toSerializableFormat();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(serializedGame);  // Write the serialized game data to the file
        } catch (IOException e) {
            System.err.println("Error saving the game to file: " + e.getMessage());
        }
    }

    public static void loadGameFromFile(File file) {
        // Implement the logic to read and parse the file to restore game state
        try {
            // Example placeholder for file reading
            System.out.println("Loading game data from: " + file.getAbsolutePath());
            // Actual file reading and game state restoration logic goes here
        } catch (Exception e) {
            System.out.println("An error occurred while loading the game: " + e.getMessage());
        }
    }
}