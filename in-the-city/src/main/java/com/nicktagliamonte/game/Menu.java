package com.nicktagliamonte.game;

public class Menu {
    GameEngine gameEngine;

    public Menu(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void displayMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Manual");
        System.out.println("2. Map");
        // ... other options
        System.out.println("9. Return to Game");
        
        int choice = gameEngine.getPlayerInputAsInt();
        
        switch (choice) {
            case 1:
                displayManual();
                break;
            case 2:
                // displayMap(); // Example for other options
                break;
            // ... handle other cases
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
        System.out.println("Game Manual:");
        System.out.println("Here are the game instructions...");
        // Display the manual content
        
        // Prompt to return to the menu
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

    private void openMap() {
        // Map logic here
        System.out.println("Opening the map...");
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
        System.out.println("Opening the crafting menu...");
    }

    private void saveGame() {
        // Save logic here
        System.out.println("Saving the game...");
    }

    private void loadGame() {
        // Load logic here
        System.out.println("Loading the game...");
    }

    private void quitGame() {
        // Quit logic here
        System.out.println("Quitting the game...");
        System.exit(0);
    }

    private void returnToGame() {
        // Logic to return to the game from the menu
        System.out.println("Returning to the game...");
        gameEngine.isInMenu = false;  // Set the game state back to normal mode
    }
}