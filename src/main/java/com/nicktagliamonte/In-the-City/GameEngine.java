import java.util.Scanner;

public class GameEngine {
    private GameState gameState = new GameState();
    private Scanner scanner = new Scanner(System.in);
    private Player player = new Player();

    public void startGame() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine();
            String[] splitInput = input.split(" ", 2);
            String commandName = splitInput[0].toLowerCase();
            String[] args = (splitInput.length > 1) ? splitInput[1].split(" ") : new String[0];

            GameCommand command = GameCommand.fromString(commandName);
            if (command != null) {
                command.execute(args, gameState);
            } else {
                System.out.println("Unknown command.");
            }

            if (commandName.equals("quit")) {
                isRunning = false;
            }
        }
        scanner.close();
        System.out.println("Thanks for playing!");
    }
}