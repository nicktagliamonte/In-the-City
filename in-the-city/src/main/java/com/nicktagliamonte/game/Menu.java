package com.nicktagliamonte.game;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.PartyMember;
import com.nicktagliamonte.items.BigMagicalTrap;
import com.nicktagliamonte.items.BigTrap;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.items.SmallMagicalTrap;
import com.nicktagliamonte.items.SmallTrap;
import com.nicktagliamonte.items.Trap;
import com.nicktagliamonte.quests.Quest;
import com.nicktagliamonte.quests.Objective;

public class Menu {
    GameEngine gameEngine;

    public Menu(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void displayMenu() {
        
        System.out.println("Main Menu:");
        
        System.out.println("1. Manual");
        
        System.out.println("2. Map");
        
        System.out.println("3. Quests");
        
        System.out.println("4. Crafting");
        
        System.out.println("5. Save");
        
        System.out.println("6. Load");
        
        System.out.println("7. Quit");
        
        System.out.println("8. Return to Game");
        
        int choice = gameEngine.getPlayerInputAsInt();
        
        switch (choice) {
            case 1:
                displayManual();
                break;
            case 2:
                displayMap();
                break;
            case 3:
                openQuests();
                break;
            case 4:
                openCrafting();
                break;
            case 5:
                saveGame();
                break;
            case 6:
                loadGame();
                break;
            case 7:
                quitGame();
                break;
            case 8:
                returnToGame();
                break;
            default:
                
                System.out.println("Invalid choice. Please try again.");
                displayMenu(); // Re-display the menu on invalid input
                break;
        }
    }

    private void displayManual() {        
        System.out.println("1. Display a list of commands available in the game.");               
        System.out.println("2. Display a combat manual");        
        System.out.println("3. Display a manual describing trade and economy in the game");
        System.out.println("4. Display a description of character classes.");        
        System.out.println("5. Return to the main menu.");
        Manual manual = new Manual();
        while (true) {
            int input = gameEngine.getPlayerInputAsInt();
            if (input == 1) {
                manual.printCommands(gameEngine.getGameState());
                
                System.out.println("");
                System.out.println("1. Display a list of commands available in the game.");               
                System.out.println("2. Display a combat manual");        
                System.out.println("3. Display a manual describing trade and economy in the game");
                System.out.println("4. Display a description of character classes.");        
                System.out.println("5. Return to the main menu.");
            } else if (input == 2) {
                manual.combatManual();
                
                System.out.println("");
                System.out.println("1. Display a list of commands available in the game.");               
                System.out.println("2. Display a combat manual");        
                System.out.println("3. Display a manual describing trade and economy in the game");
                System.out.println("4. Display a description of character classes.");        
                System.out.println("5. Return to the main menu.");
            } else if (input == 3) {
                manual.enconomyManual();
                
                System.out.println("");
                System.out.println("1. Display a list of commands available in the game.");               
                System.out.println("2. Display a combat manual");        
                System.out.println("3. Display a manual describing trade and economy in the game");
                System.out.println("4. Display a description of character classes.");        
                System.out.println("5. Return to the main menu.");
            } else if (input == 4) {
                manual.characterManual();

                System.out.println("");
                System.out.println("1. Display a list of commands available in the game.");               
                System.out.println("2. Display a combat manual");        
                System.out.println("3. Display a manual describing trade and economy in the game");
                System.out.println("4. Display a description of character classes.");        
                System.out.println("5. Return to the main menu.");
            } else if (input == 5) {
                displayMenu();
                break;
            }
            else {
                
                System.out.println("Invalid choice. Enter 5 to return to the main menu.");
            }
        }
    }

    private void displayMap() {
        
        System.out.println("There is a map of the entire game world and a map of your current room available.");
        
        System.out.println("1. Display the map of your current room.");
        
        System.out.println("2. Display the World Map");
        
        System.out.println("3. Return to the main menu.");
        WorldMap worldMap = new WorldMap();
        while (true) {
            int input = gameEngine.getPlayerInputAsInt();
            if (input == 1) {
                
                System.out.println("KEY:\nI = Item\t\tE = Exit\nF = Friend NPC\t\tP = Party Member\nN = Neutral NPC\t\tA = Adversarial NPC\nY = You\t\t\t. = Walkable Space");
                
                System.out.println("If you don't see yourself, you could just be \"behind\" another point of interest.  Try moving over a space.");
                gameEngine.getGameState().getCurrentRoom().printMap();
                
                System.out.println("1. Display the map of your current room.");
                
                System.out.println("2. Display the World Map");
                
                System.out.println("3. Return to the main menu.");
            } else if (input == 2) {
                worldMap.printMap();
                
                System.out.println("1. Display the map of your current room.");
                
                System.out.println("2. Display the World Map");
                
                System.out.println("3. Return to the main menu.");
            } else if (input == 3) {
                displayMenu();
                break;
            }
            else {
                
                System.out.println("Invalid choice. Enter 3 to return to the main menu.");
            }
        }
    }

    private void openQuests() {
        List<Quest> completedQuests = gameEngine.getGameState().getPlayer().getCompletedQuests();
        List<Quest> activeQuests = gameEngine.getGameState().getPlayer().getActiveQuests();

        if (!completedQuests.isEmpty()) {
            
            System.out.println("Completed quests:");
            for (Quest quest : completedQuests) {
                
                System.out.println(quest.getTitle());
            }
        }        

        
        System.out.println("\nActive quests:");
        for (Quest quest : activeQuests) {
            
            System.out.println(quest.getTitle());

            for (Objective objective : quest.getObjectives().values()) {
                if (!objective.getIsCompleted()) {
                    String objectiveType = objective.getType();
                    if (objectiveType.equalsIgnoreCase("dialogue")) {
                        
                        System.out.println("  Next objective: Talk to " + objective.getTarget());
                    } else if (objectiveType.equalsIgnoreCase("combat")) {
                        
                        System.out.println("  Next objective: Fight " + objective.getTarget());
                    } else if (objectiveType.equalsIgnoreCase("item")) {
                        
                        System.out.println("  Next objective: Collect " + objective.getTarget());
                    } else if (objectiveType.equalsIgnoreCase("puzzle")) {
                        
                        System.out.println("  Next objective: Solve Puzzle " + objective.getTarget());
                    } else if (objectiveType.equalsIgnoreCase("stealth")) {
                        
                        System.out.println("  Next objective: Avoid detection by " + objective.getTarget());
                    } else if (objectiveType.equalsIgnoreCase("movement")) {
                        
                        System.out.println("  Next objective: Go to " + objective.getTarget());
                    } else {
                        
                        System.out.println("Please report back to the dev that " + quest.getTitle() + " contains an objective with an invalid type.");
                    }
                    break;
                }
            }
        }
        
    }

    private void openCrafting() {
        boolean hasTechnologist = hasTechnologist();
    
        if (hasTechnologist) {
            // Display options for Technologist
            System.out.println("There are 3 types of traps you can make.");
            System.out.println("1. Small Magical Trap (1 scrap)");
            System.out.println("2. Large Magical Trap (2 scrap, 1 food)");
            System.out.println("3. Return to the main menu");
    
            while (true) {
                int input = gameEngine.getPlayerInputAsInt();
    
                if (input == 1) {
                    // Craft Small Magical Trap
                    Trap trap = new SmallMagicalTrap(gameEngine.getGameState());
                    if (checkAndCraftTrap(trap, 4, "Small Magical Trap (1 scrap)", true)) {
                        break;
                    }
                } else if (input == 2) {
                    // Craft Large Magical Trap
                    System.out.println("Note: This is a later game item that returns items which, in this demo, often will not have a use.");
                    Trap trap = new BigMagicalTrap(gameEngine.getGameState());
                    if (checkAndCraftTrap(trap, 4, "Large Magical Trap (2 scrap, 1 food)", true)) {
                        break;
                    }
                } else if (input == 3) {
                    // Return to the main menu
                    displayMenu();
                    break;
                } else {
                    System.out.println("Invalid choice. Enter 4 to return to the main menu.");
                }
            }
        } else {
            // Crafting for non-technologist
            System.out.println("There are 3 types of traps you can make.");
            System.out.println("1. Small Trap (1 scrap)");
            System.out.println("2. Large Trap (2 scrap, 1 food)");
            System.out.println("3. Return to the main menu");
    
            while (true) {
                int input = gameEngine.getPlayerInputAsInt();
    
                if (input == 1) {
                    // Craft Small Trap
                    Trap trap = new SmallTrap(gameEngine.getGameState());
                    if (checkAndCraftTrap(trap, 2, "Small Trap (1 scrap)", false)) {
                        break;
                    }
                } else if (input == 2) {
                    // Craft Large Trap (only for non-technologist)
                    System.out.println("Note: This is a later game item that returns items which, in this demo, often will not have a use.");
                    Trap trap = new BigTrap(gameEngine.getGameState());
                    if (checkAndCraftTrap(trap, 2, "Large Trap (2 scrap, 1 food)", false)) {
                        break;
                    }
                } else if (input == 3) {
                    // Return to the main menu
                    displayMenu();
                    break;
                } else {
                    System.out.println("Invalid choice. Enter 4 to return to the main menu.");
                }
            }
        }
    }

    private boolean checkAndCraftTrap(Trap trap, int xpReward, String trapDescription, boolean hasTechnologist) {
        // Get the cost of the trap (required items)
        Map<Item, Integer> requiredItems = trap.getCost();
        List<Item> playerInventory = gameEngine.getGameState().getPlayer().getInventory();
        
        // Check if the player has the required items
        for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
            Item item = entry.getKey();
            int requiredQuantity = entry.getValue();
            int playerQuantity = countItemInInventory(item, playerInventory);
            
            // If the player doesn't have enough of any required item, return false
            if (playerQuantity < requiredQuantity) {
                System.out.println("You need " + (requiredQuantity - playerQuantity) + " more " + item.getName());
                return false;
            }
        }

        // Deduct the required items from the player's inventory
        for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
            Item item = entry.getKey();
            int requiredQuantity = entry.getValue();
            removeItemsFromInventory(item, requiredQuantity, playerInventory);
        }

        // Craft the item and add it to the inventory
        gameEngine.getGameState().getPlayer().gainXP(xpReward, gameEngine.getGameState());
        gameEngine.getGameState().getPlayer().craftItem(trap);
        playerInventory.add(trap);

        return true;
    }

    private int countItemInInventory(Item item, List<Item> inventory) {
        int count = 0;
        for (Item inventoryItem : inventory) {
            if (inventoryItem.getName().equals(item.getName())) {
                count++;
            }
        }
        return count;
    }
    
    private void removeItemsFromInventory(Item item, int quantity, List<Item> inventory) {
        for (int i = 0; i < inventory.size() && quantity > 0; i++) {
            if (inventory.get(i).getName().equals(item.getName())) {
                inventory.remove(i);
                quantity--;
                i--; // Adjust the index after removal
            }
        }
    }    

    private void saveGame() {
        try {
            // Define the directory for saved games
            File saveDirectory = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator 
                                    + "main" + File.separator + "java" + File.separator + "resources" + File.separator + "saved games");
            
            // Create the directory if it doesn't exist
            if (!saveDirectory.exists()) {
                saveDirectory.mkdir();
            }
            
            // Generate a filename with the current date and time
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String saveFileName = "save_" + timestamp + ".json";
            File saveFile = new File(saveDirectory, saveFileName);
            
            // Save the game
            gameEngine.saveGameToFile(saveFile.getAbsolutePath());
            
            System.out.println("Game saved successfully at: " + saveFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("An error occurred while saving the game: " + e.getMessage());
        }
    }

    private void loadGame() {
        // Define the saved games folder path
        File saveDirectory = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator 
                                    + "main" + File.separator + "java" + File.separator + "resources" + File.separator + "saved games");
    
        // Check if the directory exists
        if (!saveDirectory.exists()) {
            System.out.println("The saved games folder does not exist.");
            return; // Exit the method
        }
    
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.requestFocus();
    
        // Create a file chooser for loading
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a save file to load");
    
        // Set the file chooser to start in the saved games folder
        fileChooser.setCurrentDirectory(saveDirectory);
    
        // Set it to file mode (only selecting files)
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    
        // Show the open dialog
        int userSelection = fileChooser.showOpenDialog(frame);
    
        // Check if the user selected a file
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
    
            System.out.println("Load file from: " + fileToLoad.getAbsolutePath());
    
            // Pass the selected file to the GameEngine for loading
            GameEngine.loadGameFromFile(fileToLoad);
        } else {
            System.out.println("Load operation was cancelled.");
        }
    
        // Close the frame
        frame.dispose();
    }    

    private void quitGame() {
        Scanner scanner = new Scanner(System.in);
        String inputString = " ";
        while (!inputString.equalsIgnoreCase("n")) {
            
            System.out.println("Quit the game? (y/n)");
            inputString = scanner.nextLine();
            if (inputString.equalsIgnoreCase("y")) {
                scanner.close();
                System.exit(0);
            } else if (inputString.equalsIgnoreCase("n")) {
                displayMenu();
            }
        }
    }

    private void returnToGame() {
        // Logic to return to the game from the menu
        
        System.out.println("Returning to the game...");
        gameEngine.isInMenu = false;  // Set the game state back to normal mode
    }

    private boolean hasTechnologist() {
        List<String> partyClasses = new ArrayList<>();
        for (NPC member : gameEngine.getGameState().getCurrentParty()) {
            PartyMember pm = (PartyMember) member;
            partyClasses.add(pm.getCharacterClass().getClassName());
        }

        return (gameEngine.getGameState().getPlayer().getCharacterClass().getClassName().equalsIgnoreCase("technologist") || 
        partyClasses.contains("technologist"));
    }
}