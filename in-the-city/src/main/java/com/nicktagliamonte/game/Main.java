package com.nicktagliamonte.game;

import java.io.File;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Main {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Do you have a save file to load from? (y/n): ");
            input = scanner.nextLine().trim().toLowerCase(); 

            if (input.equals("y")) {
                validInput = true;
                String filePath = loadPrompt();
                if (filePath != null) {
                    launchWithSave(filePath);
                } else {
                    System.out.println("Could not find the file specified. Launching new game.");
                    launchWithoutSave();
                }
            } else if (input.equals("n")) {
                validInput = true;
                launchWithoutSave();                
            } else {
                System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
            }
        }
    }

    public static void launchWithoutSave() {
        GameEngine gameEngine = new GameEngine();
        gameEngine.startGame(false, "");
    }

    public static void launchWithSave(String filePath) {
        Load load = new Load(filePath);
        GameEngine gameEngine = new GameEngine(load);
        gameEngine.startGame(true, filePath);
    }

    public static String loadPrompt() {
        // Define the saved games folder path
        File saveDirectory = new File(System.getProperty("user.dir") + File.separator + "app" + File.separator + "src" + File.separator 
                                    + "main" + File.separator + "java" + File.separator + "resources" + File.separator + "saved games");
    
        // Check if the directory exists
        if (!saveDirectory.exists()) {
            System.out.println("The saved games folder does not exist. Starting a new game.");
            launchWithoutSave();
            return null;
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
            frame.dispose();
            return fileToLoad.getAbsolutePath();
        } else {
            System.out.println("Load operation was cancelled.");
            frame.dispose();
            return null;
        }
    }    
}