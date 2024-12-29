package com.nicktagliamonte.puzzles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class MastermindPuzzleData {
    @Expose private String id;
    @Expose private String itemName;
    @Expose private int numberOfDigits;
    @Expose private String description;
    @Expose private List<Integer> solution;
    @Expose private int maxAttempts;
    @Expose private Map<Integer, String> hints;
    @Expose private int reward;
    @Expose private String completionMessage;
    @Expose private String completionLock;
    @Expose private List<Item> completionItems;

    public MastermindPuzzleData(String id, String itemName, int numberOfDigits, String description, List<Integer> solution, int maxAttempts,
            Map<Integer, String> hints, int reward, String completionMessage, String completionLock, List<Item> completionItems) {
        this.id = id;
        this.itemName = itemName;
        this.numberOfDigits = numberOfDigits;
        this.description = description;
        this.solution = solution;
        this.maxAttempts = maxAttempts;
        this.hints = hints;
        this.reward = reward;
        this.completionMessage = completionMessage;
        this.completionLock = completionLock;
        this.completionItems = completionItems;
    }

    public String getCompletionLock() {
        return completionLock;
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

    public int getNumberOfDigits() {
        return numberOfDigits;
    }

    public String getDescription() {
        return description;
    }

    public int getReward() {
        return reward;
    }

    public List<Integer> getSolution() {
        return solution;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public Map<Integer, String> getHints() {
        return hints;
    }

    public boolean checkPlayerSequence(List<Integer> playerSequence) {
        return solution.equals(playerSequence);
    }

    public String getHint() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(hints.size()) + 1;
        return hints.get(randomIndex);
    }
}