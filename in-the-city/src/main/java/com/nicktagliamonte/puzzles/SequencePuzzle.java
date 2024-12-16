package com.nicktagliamonte.puzzles;

import java.util.*;

import com.nicktagliamonte.game.GameState;
import com.nicktagliamonte.quests.Objective;
import com.nicktagliamonte.quests.Quest;

public class SequencePuzzle {
    private SequencePuzzleData puzzleData;
    private List<Integer> playerAttempt;
    private GameState gameState;

    public SequencePuzzle(SequencePuzzleData puzzleData, GameState gameState) {
        this.puzzleData = puzzleData;
        this.playerAttempt = new ArrayList<>();
        this.gameState = gameState;
    }

    public void handleTurnDial(int dialNumber) {
        SequencePuzzleData.PuzzlePiece piece = getPuzzlePieceByDial(dialNumber);
        if (piece != null) {
            if ("unmoved".equals(piece.getState())) {
                piece.state = "moved";
                playerAttempt.add(dialNumber);
                System.out.println("Dial " + dialNumber + " turned.");
                System.out.println(piece.getMovedText());
            } else {
                piece.state = "unmoved";
                playerAttempt.remove(Integer.valueOf(dialNumber));
                System.out.println("Dial " + dialNumber + " reset.");
                System.out.println(piece.getUnmovedText());
            }
        } else {
            System.out.println("Invalid dial number.");
        }
        System.out.println("Your moves so far:");
        for (int i = 0; i < playerAttempt.size(); i++){
            System.out.print(playerAttempt.get(i));
            if (i < playerAttempt.size() - 1) {
                System.out.print("->");
            }
        }
        System.out.println();
    }

    public boolean handleVerify() {
        if (puzzleData.checkPlayerSequence(playerAttempt)) {
            System.out.println("Correct! Puzzle solved.");
            gameState.getPlayer().gainXP(puzzleData.getReward(), gameState);
            for (Quest quest : gameState.getPlayer().getActiveQuests()) {
                for (Objective objective : quest.getObjectives().values()) {
                    if (objective.getType().equalsIgnoreCase("puzzle") && objective.getTarget().equalsIgnoreCase(puzzleData.getItemName())) {
                        quest.completeObjective(objective.getId());
                        break;
                    }
                }
            }
            return true;
        } else {
            System.out.println("Incorrect sequence. Try again.");
            return false;
        }
    }

    public void handleReset() {
        playerAttempt.clear();
        for (SequencePuzzleData.PuzzlePiece piece : puzzleData.getSequence()) {
            piece.state = "unmoved";
        }
        System.out.println("Puzzle reset. Try again.");
    }

    public void handleHint() {
        if (playerAttempt.isEmpty()) {
            System.out.println("Hint: Try turning any of the dials.");
        } else {
            int lastPiece = playerAttempt.get(playerAttempt.size() - 1);
            String hint = puzzleData.getHint(lastPiece);
            System.out.println("Hint: " + hint);
        }
    }

    @SuppressWarnings("resource")
    public void startPuzzleLoop() {
        Scanner scanner = new Scanner(System.in);
        String command;
        System.out.println(puzzleData.getDescription());
        
        while (true) {
            System.out.println("Enter a command (e.g., 'turn dial 1', 'verify', 'reset', 'hint', 'exit'):");
            command = scanner.nextLine();

            if (command.startsWith("turn dial")) {
                handleTurnDial(Integer.parseInt(command.split(" ")[2]));
            } else if (command.equals("verify")) {
                if (handleVerify()) {
                    break;
                }
            } else if (command.equals("reset")) {
                handleReset();
            } else if (command.equals("hint")) {
                handleHint();
            } else if (command.equals("exit")) {
                System.out.println("Exiting puzzle...");
                break;
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    private SequencePuzzleData.PuzzlePiece getPuzzlePieceByDial(int dial) {
        for (SequencePuzzleData.PuzzlePiece piece : puzzleData.getSequence()) {
            if (piece.getDial() == dial) {
                return piece;
            }
        }
        return null;
    }
}