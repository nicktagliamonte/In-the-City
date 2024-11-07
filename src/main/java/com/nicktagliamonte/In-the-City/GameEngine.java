import java.util.Scanner;

public class GameEngine {
    Scanner scanner = new Scanner(System.in);

    public void startGame() {
        boolean isRunning = true;
        while (isRunning) {
            //capture and process user input
            String command = scanner.nextLine();
            //parse the command

            //game state management
                //this will rely on classes like player, map, and gamestate
            
            //render or print outputs
            //get a message to render (some variety of methods will generate these i think, then there will be a getMessage method)
            String message = command;
            render(message);

            if (command.equalsIgnoreCase("quit")) {
                isRunning = false;
            }
        }
        scanner.close();
        System.out.println("Thanks for playing!");
    }

    public void render(String message) {
        System.out.println(message);
    }

    /**
    public void triggerEvent(String eventName) {
        switch (eventName) {
            case "findTreasure":
                System.out.println("You found a hidden treasure!");
                break;
            // Add more events as needed
        }
    } 
    */
}