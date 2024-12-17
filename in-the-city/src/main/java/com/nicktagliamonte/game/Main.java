package com.nicktagliamonte.game;

public class Main {
    public static void main(String[] args) {        
        // Instantiate the game engine
        GameEngine gameEngine = new GameEngine();
        
        // Start the game
        if (args.length == 0) {
            gameEngine.startGame(false, "");
        } else {
            gameEngine.startGame(true, "");
        }
    }
}
