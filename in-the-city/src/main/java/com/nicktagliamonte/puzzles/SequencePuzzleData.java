package com.nicktagliamonte.puzzles;

import com.google.gson.Gson;
import java.util.List;
import java.util.Map;

public class SequencePuzzleData {
    private String id;
    private int numberOfPieces;
    private String description;
    private List<PuzzlePiece> sequence;
    private List<Integer> solution;
    private Map<String, String> hints;
    private int reward;

    // Constructor
    public SequencePuzzleData(String json) {
        Gson gson = new Gson();
        SequencePuzzleData data = gson.fromJson(json, SequencePuzzleData.class);
        this.id = data.id;
        this.numberOfPieces = data.numberOfPieces;
        this.description = data.description;
        this.sequence = data.sequence;
        this.solution = data.solution;
        this.hints = data.hints;
        this.reward = data.reward;
    }

    // Getters
    public String getId() {
        return id;
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
}