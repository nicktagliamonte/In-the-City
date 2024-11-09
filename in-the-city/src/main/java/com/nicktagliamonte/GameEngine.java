package com.nicktagliamonte;
import java.util.*;

public class GameEngine {
    public Player player = new Player();
    private GameState gameState = new GameState(player, this,
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\com\\nicktagliamonte\\test_region.json", 
     "P:\\coding\\In-the-City\\in-the-city\\src\\main\\java\\com\\nicktagliamonte\\test_adjacencies.json");
    private Scanner scanner = new Scanner(System.in);
    

    public void startGame() {
        boolean isRunning = true;
        int invalidCommands = 0;

        while (isRunning) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine();
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
        scanner.close();
        System.out.println("Thanks for playing!");
    }

    public void launchMenu() {
        System.out.println("menu successfully launched");
    }

    public void saveGame() {
        //String saveData = gameState.toSerializableFormat();
        //logic to save 'saveData' to a file
        System.out.println("Game saved successfully");
    }

    public void loadGame() {
        // Logic to read saved data from a file
        String savedData = readFromFile("savefile.txt");

        if (savedData != null && !savedData.isEmpty()) {
            gameState.fromSerializableFormat(savedData);
            System.out.println("Game loaded successfully.");
        } else {
            System.out.println("Failed to load game. No save data found.");
        }
    }

    private String readFromFile(String filename) {
        // Logic to read data from a file (e.g., using FileReader, BufferedReader)
        return "Read file content here";
    }

    public void checkForRandomEvent() {
        Random random = new Random();
        int chance = random.nextInt(10);
    
        if (chance == 0) { 
            System.out.println("Success!");
        }
    }
}