package com.nicktagliamonte.puzzles;

import java.util.*;

import com.nicktagliamonte.game.GameState;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.quests.Objective;
import com.nicktagliamonte.quests.Quest;
import com.nicktagliamonte.rooms.Adjacency;

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
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Dial " + dialNumber + " turned.");
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(piece.getMovedText());
            } else {
                piece.state = "unmoved";
                playerAttempt.remove(Integer.valueOf(dialNumber));
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Dial " + dialNumber + " reset.");
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(piece.getUnmovedText());
            }
        } else {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Invalid dial number.");
        }
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Your moves so far:");
        for (int i = 0; i < playerAttempt.size(); i++){
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.print(playerAttempt.get(i));
            if (i < playerAttempt.size() - 1) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.print(" -> ");
            }
        }
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }

    public boolean handleVerify() {
        if (puzzleData.checkPlayerSequence(playerAttempt)) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Correct! Puzzle solved.");
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
            return true;
        } else {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Incorrect sequence. Try again.");
            return false;
        }
    }

    public void handleReset() {
        playerAttempt.clear();
        for (SequencePuzzleData.PuzzlePiece piece : puzzleData.getSequence()) {
            piece.state = "unmoved";
        }
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Puzzle reset. Try again.");
    }

    public void handleHint() {
        if (playerAttempt.isEmpty()) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Hint: Try turning any of the dials.");
        } else {
            int lastPiece = playerAttempt.get(playerAttempt.size() - 1);
            String hint = puzzleData.getHint(lastPiece);
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Hint: " + hint);
        }
    }

    @SuppressWarnings("resource")
    public void startPuzzleLoop() {
        Scanner scanner = new Scanner(System.in);
        String command;
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(puzzleData.getDescription());
        
        while (true) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Enter a command (e.g., 'turn dial 1', 'verify', 'reset', 'hint', 'exit'):");
            command = scanner.nextLine();

            if (command.startsWith("turn dial")) {
                String[] commandArray = command.split(" ");
                if (commandArray.length < 3) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Enter a dial to turn");
                } else {
                    handleTurnDial(Integer.parseInt(command.split(" ")[2]));
                }
            } else if (command.equals("verify")) {
                if (handleVerify()) {
                    break;
                }
            } else if (command.equals("reset")) {
                handleReset();
            } else if (command.equals("hint")) {
                handleHint();
            } else if (command.equals("exit")) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Exiting puzzle...");
                break;
            } else {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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