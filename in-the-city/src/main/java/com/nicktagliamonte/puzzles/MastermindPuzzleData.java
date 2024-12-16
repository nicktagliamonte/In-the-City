package com.nicktagliamonte.puzzles;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MastermindPuzzleData {
    private String id;
    private int numberOfDigits;
    private String description;
    private List<Integer> solution;
    private int maxAttempts;
    private Map<Integer, String> hints;
    private int reward;

    public MastermindPuzzleData(String id, int numberOfDigits, String description, List<Integer> solution, int maxAttempts,
            Map<Integer, String> hints, int reward) {
        this.id = id;
        this.numberOfDigits = numberOfDigits;
        this.description = description;
        this.solution = solution;
        this.maxAttempts = maxAttempts;
        this.hints = hints;
        this.reward = reward;
    }

    public String getId() {
        return id;
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