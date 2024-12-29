package com.nicktagliamonte.puzzles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class SequencePuzzleData {
    @Expose private String id;
    @Expose private String itemName;
    @Expose private int numberOfPieces;
    @Expose private String description;
    @Expose private List<PuzzlePiece> sequence;
    @Expose private List<Integer> solution;
    @Expose private Map<String, String> hints;
    @Expose private int reward;
    @Expose private String completionMessage;
    @Expose private String completionLock;
    @Expose private List<Item> completionItems;

    public SequencePuzzleData(String id, String itemName, int numberOfPieces, String description, List<PuzzlePiece> sequence,
                          List<Integer> solution, Map<String, String> hints, int reward, String completionMessage,
                          String completionLock, List<Item> completionItems) {
        this.id = id;
        this.itemName = itemName;
        this.numberOfPieces = numberOfPieces;
        this.description = description;
        this.sequence = sequence;
        this.solution = solution;
        this.hints = hints;
        this.reward = reward;
        this.completionMessage = completionMessage;
        this.completionLock = completionLock;
        this.completionItems = completionItems;
    }

    public List<Item> getCompletionItems() {
        List<Item> returnArray = new ArrayList<>();
        for (Object item : completionItems) {
            if (item instanceof Item) {
                returnArray.add((Item) item); // Add directly if it's an Item
            } else if (item instanceof List<?>) {
                for (Object subItem : (List<?>) item) {
                    if (subItem instanceof Item) {
                        returnArray.add((Item) subItem); // Add items from nested lists
                    } else {
                        throw new IllegalArgumentException("Invalid item type in completionItems");
                    }
                }
            } else {
                throw new IllegalArgumentException("Invalid item type in completionItems");
            }
        }
        return returnArray;
    }

    public String getCompletionMessage() {
        return completionMessage;
    }

    public String getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getNumberOfPieces() {
        return numberOfPieces;
    }

    public List<PuzzlePiece> getSequence() {
        return sequence;
    }

    public List<Integer> getSolution() {
        return solution;
    }

    public Map<String, String> getHints() {
        return hints;
    }

    public String getDescription() {
        return description;
    }

    public int getReward() {
        return reward;
    }

    // Class to represent each puzzle piece (dial)
    public static class PuzzlePiece {
        private int dial;
        private int correctPosition;
        String state;
        String movedText;
        String unmovedText;

        // Getters
        public int getDial() {
            return dial;
        }

        public int getCorrectPosition() {
            return correctPosition;
        }

        public String getState() {
            return state;
        }

        public String getMovedText() {
            return movedText;
        }

        public String getUnmovedText() {
            return unmovedText;
        }
    }

    public boolean checkPlayerSequence(List<Integer> playerSequence) {
        return solution.equals(playerSequence);
    }

    public String getHint(int piece) {
        return hints.get(String.valueOf(piece));
    }

    public String getCompletionLock() {
        return completionLock;
    }
}