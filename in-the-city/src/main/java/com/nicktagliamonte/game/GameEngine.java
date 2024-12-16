package com.nicktagliamonte.game;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.*;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.quests.Objective;
import com.nicktagliamonte.quests.Quest;

import java.io.File;
import java.util.*;

public class GameEngine {
    public Player player;
    public boolean isInMenu;
    @Expose private Menu menu;
    @Expose private GameState gameState = new GameState(this,
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\resources\\json\\regions\\test_region.json", 
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\resources\\json\\adjacencies\\test_adjacencies.json", 
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\resources\\json\\items\\test_items.json", 
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\resources\\json\\people\\test_people.json",
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\resources\\json\\dialogue\\test_dialogue.json");
    @Expose private Scanner scanner = new Scanner(System.in);
    @Expose private GameTimer timer;
    

    public void startGame() {
        boolean isRunning = true;
        int invalidCommands = 0;
        isInMenu = false;
        menu = new Menu(this);
        timer = new GameTimer(gameState);
        int turnsInStealth = 0;

        while (isRunning) {
            if (isInMenu) {
                menu.displayMenu();
            } else {
                if (timer.getElapsedTime() >= 120) {
                    timer.checkForEvent();
                }
                timer.checkForHunger();
                timer.checkForThirst();
                System.out.print("Enter command: ");
                String input = scanner.nextLine().trim();
                String[] splitInput = input.split(" ", 2);
                String commandName = splitInput[0].toLowerCase();
                String[] args = (splitInput.length > 1) ? new String[] { splitInput[1] } : new String[0];

                if (!gameState.itemContext.equals("") && 
                        !(commandName.equalsIgnoreCase("Examine") || commandName.equalsIgnoreCase("Use") || commandName.equalsIgnoreCase("Equip") || commandName.equalsIgnoreCase("Take"))) {
                    gameState.itemContext = "";
                }

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

                //Quest objective completion check
                List<Quest> activeQuests = gameState.getPlayer().getActiveQuests();

                int turnsNeeded;

                for (Quest quest : activeQuests) {
                    for (Objective objective : quest.getObjectives().values()) {
                        if (!objective.getIsCompleted()) {
                            String objectiveType = objective.getType();
                            if (objectiveType.equalsIgnoreCase("item")) {
                                List<Item> currentInventory = gameState.getPlayer().getInventory();
                                for (Item item : currentInventory) {
                                    if (item.getName().equalsIgnoreCase(objective.getTarget())) {
                                        quest.completeObjective(objective.getId());
                                        break;
                                    }
                                }
                            } else if (objectiveType.equalsIgnoreCase("movement")) {
                                if (gameState.getCurrentRoom().getName().equalsIgnoreCase(objective.getTarget())) {
                                    quest.completeObjective(objective.getId());
                                    break;
                                }
                            } else if (objectiveType.equalsIgnoreCase("stealth")) {
                                turnsNeeded = Integer.valueOf(objective.getTarget());
                                if (gameState.getPlayer().getIsHiding()) {
                                    turnsInStealth++;
                                    if (turnsInStealth == turnsNeeded) {
                                        quest.completeObjective(objective.getId());
                                        break;
                                    }
                                } else {
                                    turnsInStealth = 0;
                                    break;
                                }                                
                            }
                        }
                    }
                }

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
            try {
                System.out.print("Enter your choice: ");
                input = Integer.parseInt(scanner.nextLine().trim()); // Parse input as an integer
                return input; // Return the input if it's an integer
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

    public void checkForRandomEvent() {
        Random random = new Random();
        int chance = random.nextInt(10);
    
        if (chance == 0) { 
            System.out.println("Success!");
        }
    }
}