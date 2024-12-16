package com.nicktagliamonte.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.game.GameState;
import com.nicktagliamonte.items.Item;

public class Quest {
    @Expose private String questId;
    @Expose private String title;
    @Expose private String description;
    @Expose private Map<String, Objective> objectives;
    @Expose private String status;  // "inactive", "active", "complete"
    @Expose private List<Item> rewards;
    @Expose private GameState gameState;
    @Expose private boolean isPrimary;
    @Expose private String completionMessage;
    @Expose private String newRegionFilePath;
    @Expose private String newAdjacencyFilePath;
    @Expose private String newItemsFilePath;
    @Expose private String newPeopleFilePath;
    @Expose private String newDialogueFilePath;

    public Quest(String questId, String title, String description, Boolean isPrimary, 
                List<Objective> objectives, List<Item> rewards, String completionMessage, 
                String newRegionFilePath, String newAdjacencyFilePath, String newItemsFilePath, 
                String newPeopleFilePath, String newDialogueFilePath, GameState gameState) {
        this.questId = questId;
        this.title = title;
        this.description = description;
        this.isPrimary = isPrimary;
        this.status = "inactive"; // Default status
        this.objectives = new HashMap<>();
        for (Objective objective : objectives) {
            this.objectives.put(objective.getId(), objective);
        }
        this.rewards = new ArrayList<>();
        for (Item item : rewards) {
            this.rewards.add(item);
        }        
        this.gameState = gameState;
        this.completionMessage = completionMessage;
    }

    public Quest() {
        //method stub for easier gson deserialization
    }
    
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setCompletionMessage(String completionMessage) {
        this.completionMessage = completionMessage;
    }

    public String getCompletionMessage() {
        return completionMessage;
    }

    public List<Item> getRewards() {
        return rewards;
    }

    public void setRewards(List<Item> rewards) {
        this.rewards = rewards;
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Objective> getObjectives() {
        return objectives;
    }

    public void setObjectives(Map<String, Objective> objectives) {
        this.objectives = objectives;
    }

    public void checkAndCompleteQuest() {
        if (checkProgress()) {
            setStatus("complete");
            giveRewards();
        }
    }

    public boolean checkProgress() {
        for (Objective objective : objectives.values()) {
            if (!objective.getIsCompleted()) {
                return false;
            }
        }
        this.status = "complete";
        return true;
    }

    public void completeObjective(String objectiveId) {
        if (objectives.containsKey(objectiveId)) {
            Objective objective = objectives.get(objectiveId);
            objective.setIsCompleted(true);
            System.out.println(objective.getCompletionMessage());
            if (checkProgress()) {
                System.out.println(completionMessage);
                giveRewards();
                if (isPrimary) {
                    gameState.updateRegion(newRegionFilePath, newAdjacencyFilePath, newItemsFilePath, newPeopleFilePath, newDialogueFilePath);
                }
            }
        }
    }    

    public void giveRewards() {
        if ("complete".equals(status)) {
            if (isPrimary) {
                System.out.println("You receive " + gameState.getPlayer().getNextLevelXp() + " xp");
                gameState.getPlayer().gainXP(gameState.getPlayer().getNextLevelXp(), gameState);
            } else {
                System.out.println("You receive " + gameState.getPlayer().getNextLevelXp() / 3 + " xp");
                gameState.getPlayer().gainXP(gameState.getPlayer().getNextLevelXp() / 3, gameState);
            }
            for (Item reward : rewards) {
                System.out.println("You received: " + reward.getName());
                gameState.safeZoneInventory.addItemToInventory(reward);
            }
        }
    }
}