package com.nicktagliamonte.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.PartyMember;

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
        System.out.println("4. Equipment");
        System.out.println("5. Crafting");
        System.out.println("6. Save");
        System.out.println("7. Load");
        System.out.println("8. Quit");
        System.out.println("9. Return to Game");
        
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
                openEquipment();
                break;
            case 5:
                openCrafting();
                break;
            case 6:
                saveGame();
                break;
            case 7:
                loadGame();
                break;
            case 8:
                quitGame();
                break;
            case 9:
                returnToGame();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayMenu(); // Re-display the menu on invalid input
                break;
        }
    }

    private void displayManual() {
        //think about creating a manual class and having some options.
        //like a description of commands, a description of items/traps, more if you think of more
        System.out.println("Game Manual:");
        System.out.println("No command is case sensitive.  Caps are used below to indicate which words are commands and arguments.");
        System.out.println("LOOK, look, and LoOk are all valid.");
        System.out.println("This game is long and is meant to be somewhat adversarial towards you, the player.");
        System.out.println("But there is a walkthrough available on GitHub if you're frustrated, or more interested in the story than the gameplay.");
        System.out.println("========================================");
        System.out.println(" ___________________");
        System.out.println("|    ORIENTATION    |");
        System.out.println("|___________________|");
        System.out.println("   -LOOK: Prints a description of your surroundings, as well as the items, characters, and pathways accessible to you.");
        System.out.println("   -LOCATE: Describes your current location and gives directions to the nearest safe zone and economic zone.");
        System.out.println("   -MENU: You used this command to get to this menu.");
        System.out.println(" ___________________");
        System.out.println("| MOVEMENT COMMANDS |");
        System.out.println("|___________________|");
        System.out.println("   -MOVE [DIRECTION]: Moves 1 step in the direction specified by [DIRECTION].\n\tValid DIRECTIONS are UP, NORTH, DOWN, SOUTH, LEFT, WEST, RIGHT, EAST.");
        System.out.println("   -MOVE [DIRECTION] [NUMBER OF STEPS]: Moves [NUMBER OF STEPS] steps in the direction specified by [DIRECTION].\n\tValid DIRECTIONS are UP, NORTH, DOWN, SOUTH, LEFT, WEST, RIGHT, EAST.\n\tValid NUMBERS OF STEPS are integers greater than or equal to 1.");
        System.out.println("   -MOVE TO [WAYPOINT]: Moves to the WAYPOINT specified by [WAYPOINT].\n\tValid WAYPOINTS are the names of characters, items, or pathways you currently have access to.\n\tUse LOOK for a list of such waypoints.");
        System.out.println("   -ENTER [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location.\n\tCurrently available ROOM NAMES can be found using the LOOK command.");
        System.out.println("   -ASCEND [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location and is accessed via staircase going up.\n\tCurrently available ROOM NAMES can be found using the LOOK command.");
        System.out.println("   -DESCEND [ROOM NAME]: Moves immediately to the room specified by [ROOM NAME], if it adjoins your current location and is accessed via staircase going down.\n\tCurrently available ROOM NAMES can be found using the LOOK command.");
        System.out.println(" _____________________");
        System.out.println("| OBJECT INTERACTIONS |");
        System.out.println("|_____________________|");
        System.out.println("   EXAMINE [ITEM NAME]: Gives a more detailed description of the item specified by [ITEM NAME]\n\tUse LOOK and/or INVENTORY to get a list of available items.");
        System.out.println("   TAKE [ITEM NAME]: Adds [ITEM NAME] to the player/party inventory, if there is room.");
        System.out.println("   USE [ITEM NAME]: Uses [ITEM NAME], if it has a use.");
        System.out.println("   DROP [ITEM NAME]: Removes [ITEM NAME] from inventory, if there is an item of such name in inventory");
        System.out.println(" ________________________");
        System.out.println("| CHARACTER INTERACTIONS |");
        System.out.println("|________________________|");
        System.out.println("   TALK TO [CHARACTER NAME]: Enters Dialogue with [CHARACTER NAME].");
        System.out.println("   JOIN [CHARACTER NAME]: Adds [CHARACTER NAME] to the party, if there is not currently a party member of the same class as [CHARACTER NAME].\n\tThis can only be undone if [CHARACTER NAME] dies.");
        System.out.println("   FIGHT [CHARACTER NAME]: Enters Combat with [CHARACTER NAME].");
        System.out.println("   HINT [CHARACTER NAME]: Gets a hint from [CHARACTER NAME], if they have one to offer and you have one to receive.");
        System.out.println("   HIDE: Attempts to hide or conceal the presence of the party, reducing detectability and the chance of unwanted interactions.");
        System.out.println("========================================");
        System.out.println("\nEnter 1 to return to the main menu.");
    
        while (true) {
            int input = gameEngine.getPlayerInputAsInt();
            if (input == 1) {
                displayMenu(); // Return to the main menu
                break; // Exit the loop after returning to the menu
            } else {
                System.out.println("Invalid choice. Enter 1 to return to the main menu.");
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
                gameEngine.getGameState().getcurrentRoom().printMap();
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
        // Quests logic here
        System.out.println("Opening the quests menu...");
    }

    private void openEquipment() {
        // Equipment logic here
        System.out.println("Opening the equipment menu...");
    }

    private void openCrafting() {
        // Crafting logic here
        if (hasTechnologist()) {
            System.out.println("There are 3 types of traps you can make.");
            System.out.println("1. Small Magical Trap\t");
            System.out.println("2. Large Magical Trap");
            System.out.println("3. Defensive Magical Trap");
            System.out.println("4. Return to the main menu");

            while (true) {
                int input = gameEngine.getPlayerInputAsInt();
                if (input == 1) {
                    //TODO: make a small magical trap
                } else if (input == 2) {
                    //TODO: make a large magical trap
                } else if (input == 3) {
                    //TODO: make a defensive magical trap
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
            System.out.println("1. Small Trap\t");
            System.out.println("2. Large Trap");
            System.out.println("3. Defensive Trap");
            System.out.println("4. Return to the main menu");
            while (true) {
                int input = gameEngine.getPlayerInputAsInt();
                if (input == 1) {
                    //TODO: make a small trap
                } else if (input == 2) {
                    //TODO: make a large trap
                } else if (input == 3) {
                    //TODO: make a defensive trap
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
        while (true) {
            System.out.println("Quit the game? (y/n)");
            String inputString = scanner.nextLine();
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