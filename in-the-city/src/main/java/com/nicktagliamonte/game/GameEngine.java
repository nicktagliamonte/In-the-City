package com.nicktagliamonte.game;
import com.nicktagliamonte.characters.*;

import java.io.File;
import java.util.*;

public class GameEngine {
    public Player player;
    public boolean isInMenu;
    private Menu menu;
    private GameState gameState = new GameState(this,
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\resources\\json\\regions\\test_region.json", 
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\resources\\json\\adjacencies\\test_adjacencies.json", 
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\resources\\json\\items\\test_items.json", 
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\resources\\json\\people\\test_people.json",
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\resources\\json\\dialogue\\test_dialogue.json");
    private Scanner scanner = new Scanner(System.in);
    

    public void startGame() {
        boolean isRunning = true;
        int invalidCommands = 0;
        isInMenu = false;
        menu = new Menu(this);

        while (isRunning) {
            if (isInMenu) {
                menu.displayMenu();
            } else {
                System.out.print("Enter command: ");
                String input = scanner.nextLine().trim();
                String[] splitInput = input.split(" ", 2);
                String commandName = splitInput[0].toLowerCase();
                String[] args = (splitInput.length > 1) ? new String[] { splitInput[1] } : new String[0];

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