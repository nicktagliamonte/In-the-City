package com.nicktagliamonte.puzzles;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.nicktagliamonte.game.GameState;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.quests.Objective;
import com.nicktagliamonte.quests.Quest;
import com.nicktagliamonte.rooms.Adjacency;

public class MastermindPuzzle {
    private MastermindPuzzleData puzzleData;
    private int attemptsLeft;
    private boolean isSolved = false;
    private GameState gameState;

    public MastermindPuzzle(MastermindPuzzleData puzzleData, GameState gameState) {
        this.puzzleData = puzzleData;
        this.attemptsLeft = puzzleData.getMaxAttempts();
        this.gameState = gameState;
    }

    // Method to start the puzzle loop
    @SuppressWarnings("resource")
    public void startPuzzleLoop() {
        Scanner scanner = new Scanner(System.in);

        while (attemptsLeft > 0 && !isSolved) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("\nAttempts left: " + attemptsLeft);
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Enter your guess (format: space-separated numbers, e.g. '1 2 3 4'):");
            
            // Get player input (e.g., "1 2 3 4")
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Exiting the puzzle.");
                break; // Exit the puzzle loop if the player types "exit"
            }

            // Convert input string to a List<Integer> (player's guess)
            List<Integer> guess = parseGuess(input);
            if (guess == null) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Invalid input. Please enter valid numbers.");
                continue; // Skip this iteration and ask for input again
            }

            // Process the guess and provide feedback
            String feedback = processGuess(guess);
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(feedback);

            // Check if the puzzle is solved
            if (isPuzzleSolved(guess)) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Congratulations! You've solved the puzzle.");
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(puzzleData.getCompletionMessage());
                gameState.getPlayer().gainXP(puzzleData.getReward(), gameState);
                for (Quest quest : gameState.getPlayer().getActiveQuests()) {
                    for (Objective objective : quest.getObjectives().values()) {
                        if (objective.getType().equalsIgnoreCase("puzzle") && objective.getTarget().equalsIgnoreCase(puzzleData.getItemName())) {
                            quest.completeObjective(objective.getId());
                            break;
                        }
                    }
                }

                for (Item reward : puzzleData.getCompletionItems()) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("You received: " + reward.getName() + " in safe zone inventory");
                    gameState.safeZoneInventory.addItemToInventory(reward);
                }

                if (!puzzleData.getCompletionLock().equals("")) {
                    for (Adjacency adj : gameState.getCurrentRoom().getAdjacentRooms()) {
                        if (adj.getAdjoiningRoomName().equalsIgnoreCase(puzzleData.getCompletionLock())) {
                            adj.setIsLocked(false);
                        }
                    }
                }
                isSolved = true; // Exit the loop
            }

            attemptsLeft--; // Reduce attempts after each guess
        }

        // If the loop ends without solving the puzzle
        if (!isSolved) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You've run out of attempts. Better luck next time!");
        }
    }

    // Method to process a guess
    public String processGuess(List<Integer> guess) {
        // Compare the guess to the solution and return feedback
        return giveFeedback(guess);
    }

    // Method to give feedback on the guess
    private String giveFeedback(List<Integer> guess) {
        int correctPosition = 0;
        int correctNumber = 0;

        // Check for correct numbers in the correct position
        for (int i = 0; i < guess.size(); i++) {
            if (guess.get(i).equals(puzzleData.getSolution().get(i))) {
                correctPosition++;
            }
        }

        // Check for correct numbers in the wrong position
        for (int i = 0; i < guess.size(); i++) {
            if (contains(puzzleData.getSolution(), guess.get(i)) && !guess.get(i).equals(puzzleData.getSolution().get(i))) {
                correctNumber++;
            }
        }

        // Return the feedback string
        return correctPosition + " correct numbers in the correct position, " +
                correctNumber + " correct numbers in the wrong position.";
    }

    // Helper function to check if the number exists in the solution
    private boolean contains(List<Integer> solution, Integer number) {
        return solution.contains(number);
    }

    public boolean isPuzzleSolved(List<Integer> guess) {
        // Check if the guess is correct
        return guess.equals(puzzleData.getSolution());
    }

    // Convert a string input into a List<Integer> guess
    private List<Integer> parseGuess(String input) {
        String[] tokens = input.split(" ");
        try {
            List<Integer> guess = new ArrayList<>();
            for (String token : tokens) {
                guess.add(Integer.parseInt(token));
            }
            return guess;
        } catch (NumberFormatException e) {
            return null; // Return null if parsing fails (invalid input)
        }
    }
}