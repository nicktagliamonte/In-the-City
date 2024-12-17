package com.nicktagliamonte.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.PartyMember;
import com.nicktagliamonte.items.Item;
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
        System.out.println("2. Display a description of the various items available in the game.");        
        System.out.println("3. Display a combat manual");        
        System.out.println("4. Display a manual describing trade and economy in the game");
        System.out.println("5. Display a description of character classes.");        
        System.out.println("6. Return to the main menu.");
        Manual manual = new Manual();
        while (true) {
            int input = gameEngine.getPlayerInputAsInt();
            if (input == 1) {
                manual.printCommands();
                System.out.println("1. Display a list of commands available in the game.");        
                System.out.println("2. Display a description of the various items available in the game.");        
                System.out.println("3. Display a combat manual");        
                System.out.println("4. Display a manual describing trade and economy in the game");
                System.out.println("5. Display a description of character classes.");        
                System.out.println("6. Return to the main menu.");
            } else if (input == 2) {
                manual.printItems();
                
                System.out.println("");
                System.out.println("1. Display a list of commands available in the game.");        
                System.out.println("2. Display a description of the various items available in the game.");        
                System.out.println("3. Display a combat manual");        
                System.out.println("4. Display a manual describing trade and economy in the game");
                System.out.println("5. Display a description of character classes.");        
                System.out.println("6. Return to the main menu.");
            } else if (input == 3) {
                manual.combatManual();
                
                System.out.println("");
                System.out.println("1. Display a list of commands available in the game.");        
                System.out.println("2. Display a description of the various items available in the game.");        
                System.out.println("3. Display a combat manual");        
                System.out.println("4. Display a manual describing trade and economy in the game");
                System.out.println("5. Display a description of character classes.");        
                System.out.println("6. Return to the main menu.");
            } else if (input == 4) {
                manual.enconomyManual();
                
                System.out.println("");
                System.out.println("1. Display a list of commands available in the game.");        
                System.out.println("2. Display a description of the various items available in the game.");        
                System.out.println("3. Display a combat manual");        
                System.out.println("4. Display a manual describing trade and economy in the game");
                System.out.println("5. Display a description of character classes.");        
                System.out.println("6. Return to the main menu.");
            } else if (input == 5) {
                manual.characterManual();

                System.out.println("");
                System.out.println("1. Display a list of commands available in the game.");        
                System.out.println("2. Display a description of the various items available in the game.");        
                System.out.println("3. Display a combat manual");        
                System.out.println("4. Display a manual describing trade and economy in the game");
                System.out.println("5. Display a description of character classes.");        
                System.out.println("6. Return to the main menu.");
            } else if (input == 6) {
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
        // Crafting logic here
        if (hasTechnologist()) {
            
            System.out.println("There are 3 types of traps you can make.");
            
            System.out.println("1. Small Magical Trap (1 scrap)");
            
            System.out.println("2. Large Magical Trap (2 scrap, 1 food)");
            
            System.out.println("3. Defensive Magical Trap (3 scrap, 2 fuel)");
            
            System.out.println("4. Return to the main menu");

            while (true) {
                int input = gameEngine.getPlayerInputAsInt();
                
                if (input == 1) {
                    //TODO: make a small magical trap
                    gameEngine.getGameState().getPlayer().gainXP(4, gameEngine.getGameState());
                    
                    System.out.println("There are 3 types of traps you can make.");
                    
                    System.out.println("1. Small Magical Trap (1 scrap)");
                    
                    System.out.println("2. Large Magical Trap (2 scrap, 1 food)");
                    
                    System.out.println("3. Defensive Magical Trap (3 scrap, 2 fuel)");
                    
                    System.out.println("4. Return to the main menu");
                } else if (input == 2) {
                    //TODO: make a large magical trap
                    gameEngine.getGameState().getPlayer().gainXP(4, gameEngine.getGameState());
                    
                    System.out.println("There are 3 types of traps you can make.");
                    
                    System.out.println("1. Small Magical Trap (1 scrap)");
                    
                    System.out.println("2. Large Magical Trap (2 scrap, 1 food)");
                    
                    System.out.println("3. Defensive Magical Trap (3 scrap, 2 fuel)");
                    
                    System.out.println("4. Return to the main menu");
                } else if (input == 3) {
                    //TODO: make a defensive magical trap
                    gameEngine.getGameState().getPlayer().gainXP(4, gameEngine.getGameState());
                    
                    System.out.println("There are 3 types of traps you can make.");
                    
                    System.out.println("1. Small Magical Trap (1 scrap)");
                    
                    System.out.println("2. Large Magical Trap (2 scrap, 1 food)");
                    
                    System.out.println("3. Defensive Magical Trap (3 scrap, 2 fuel)");
                    
                    System.out.println("4. Return to the main menu");
                } else if (input == 4) {
                    displayMenu();
                    break;
                }
                else {
                    
                    System.out.println("Invalid choice. Enter 4 to return to the main menu.");
                }
            }
        } else {
            
            System.out.println("There are 3 types of traps you can make.");
            
            System.out.println("1. Small Trap (1 scrap)");
            
            System.out.println("2. Large Trap (2 scrap, 1 food)");
            
            System.out.println("3. Defensive Trap (3 scrap, 2 fuel)");
            
            System.out.println("4. Return to the main menu");

            while (true) {
                int input = gameEngine.getPlayerInputAsInt();
                
                if (input == 1) {
                    gameEngine.getGameState().getPlayer().gainXP(2, gameEngine.getGameState());
                    Trap trap = new SmallTrap(gameEngine.getGameState());
                    gameEngine.getGameState().getPlayer().craftItem(trap);
                    
                    List<Item> inventory = gameEngine.getGameState().getPlayer().getInventory();
                    inventory.add(trap);
                    
                    System.out.println("There are 3 types of traps you can make.");
                    
                    System.out.println("1. Small Trap (1 scrap)");
                    
                    System.out.println("2. Large Trap (2 scrap, 1 food)");
                    
                    System.out.println("3. Defensive Trap (3 scrap, 2 fuel)");
                    
                    System.out.println("4. Return to the main menu");
                } else if (input == 2) {
                    gameEngine.getGameState().getPlayer().gainXP(2, gameEngine.getGameState());
                    //TODO: make a large trap
                    
                    System.out.println("1. Small Trap (1 scrap)");
                    
                    System.out.println("2. Large Trap (2 scrap, 1 food)");
                    
                    System.out.println("3. Defensive Trap (3 scrap, 2 fuel)");
                    
                    System.out.println("4. Return to the main menu");
                } else if (input == 3) {
                    gameEngine.getGameState().getPlayer().gainXP(2, gameEngine.getGameState());
                    //TODO: make a defensive trap
                    
                    System.out.println("1. Small Trap (1 scrap)");
                    
                    System.out.println("2. Large Trap (2 scrap, 1 food)");
                    
                    System.out.println("3. Defensive Trap (3 scrap, 2 fuel)");
                    
                    System.out.println("4. Return to the main menu");
                } else if (input == 4) {
                    displayMenu();
                    break;
                }
                else {
                    
                    System.out.println("Invalid choice. Enter 4 to return to the main menu.");
                }
            }
        }
    }

    private void saveGame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false);
        frame.setAlwaysOnTop(true);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a save location");
        
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            System.out.println("Save file to: " + fileToSave.getAbsolutePath());
            
            //TODO: add logic to save the game state to this file later
        } else {
            
            System.out.println("Save operation was cancelled.");
        }

        frame.dispose();
    }

    private void loadGame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    
        // Create a file chooser for loading
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a save file to load");
        
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