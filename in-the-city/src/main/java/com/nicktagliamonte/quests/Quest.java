package com.nicktagliamonte.quests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.Spells.Spell;
import com.nicktagliamonte.game.GameState;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.items.Weapon;

public class Quest {
    @Expose
    private String questId;
    @Expose
    private String title;
    @Expose
    private String description;
    @Expose
    private Map<String, Objective> objectives;
    @Expose
    private String status; // "inactive", "active", "complete"
    @Expose
    private List<Item> rewards;
    @Expose
    private transient GameState gameState;
    @Expose
    private boolean isPrimary;
    @Expose
    private boolean isFinal;
    @Expose
    private String completionMessage;
    @Expose
    private List<Item> itemToChoose;
    @Expose
    private Spell spellToChoose;
    @Expose
    private String questGiverName;
    @Expose
    private String newRegionFilePath;
    @Expose
    private String newAdjacencyFilePath;
    @Expose
    private String newItemsFilePath;
    @Expose
    private String newPeopleFilePath;
    @Expose
    private String newDialogueFilePath;

    public Quest(String questId, String title, String description, Boolean isPrimary, boolean isFinal,
            List<Objective> objectives, List<Item> rewards, String completionMessage, List<Item> itemToChoose,
            Spell spellToChoose, String newRegionFilePath, String newAdjacencyFilePath, String newItemsFilePath,
            String newPeopleFilePath, String newDialogueFilePath, String questGiverName, GameState gameState) {
        this.questId = questId;
        this.title = title;
        this.description = description;
        this.isPrimary = isPrimary;
        this.isFinal = isFinal;
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
        this.itemToChoose = itemToChoose;
        this.spellToChoose = spellToChoose;
        this.questGiverName = questGiverName;
        this.newRegionFilePath = newRegionFilePath;
        this.newAdjacencyFilePath = newAdjacencyFilePath;
        this.newItemsFilePath = newItemsFilePath;
        this.newPeopleFilePath = newPeopleFilePath;
        this.newDialogueFilePath = newDialogueFilePath;
    }

    public Quest(String questId, String title, String description, Boolean isPrimary, boolean isFinal,
            List<Objective> objectives, List<Item> rewards, String completionMessage, List<Item> itemToChoose,
            Spell spellToChoose, String newRegionFilePath, String newAdjacencyFilePath, String newItemsFilePath,
            String newPeopleFilePath, String newDialogueFilePath, String questGiverName) {
        this.questId = questId;
        this.title = title;
        this.description = description;
        this.isPrimary = isPrimary;
        this.isFinal = isFinal;
        this.status = "inactive"; // Default status
        this.objectives = new HashMap<>();
        for (Objective objective : objectives) {
            this.objectives.put(objective.getId(), objective);
        }
        this.rewards = new ArrayList<>();
        for (Item item : rewards) {
            this.rewards.add(item);
        }
        this.completionMessage = completionMessage;
        this.itemToChoose = itemToChoose;
        this.spellToChoose = spellToChoose;
        this.questGiverName = questGiverName;
        this.newRegionFilePath = newRegionFilePath;
        this.newAdjacencyFilePath = newAdjacencyFilePath;
        this.newItemsFilePath = newItemsFilePath;
        this.newPeopleFilePath = newPeopleFilePath;
        this.newDialogueFilePath = newDialogueFilePath;
    }

    public void flattenRewards(List<Item> listToFlatten) {
        List<Item> flattenedRewards = new ArrayList<>();
        for (Object itemOrList : listToFlatten) {
            if (itemOrList instanceof Iterable<?>) {
                for (Object nestedItem : (Iterable<?>) itemOrList) {
                    flattenedRewards.add((Item) nestedItem);
                }
            } else {
                flattenedRewards.add((Item) itemOrList);
            }
        }

        this.rewards = flattenedRewards;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public String getNewRegionFilePath() {
        return newRegionFilePath;
    }

    public void setNewRegionFilePath(String newRegionFilePath) {
        this.newRegionFilePath = newRegionFilePath;
    }

    public String getNewAdjacencyFilePath() {
        return newAdjacencyFilePath;
    }

    public void setNewAdjacencyFilePath(String newAdjacencyFilePath) {
        this.newAdjacencyFilePath = newAdjacencyFilePath;
    }

    public String getNewItemsFilePath() {
        return newItemsFilePath;
    }

    public void setNewItemsFilePath(String newItemsFilePath) {
        this.newItemsFilePath = newItemsFilePath;
    }

    public String getNewPeopleFilePath() {
        return newPeopleFilePath;
    }

    public void setNewPeopleFilePath(String newPeopleFilePath) {
        this.newPeopleFilePath = newPeopleFilePath;
    }

    public String getNewDialogueFilePath() {
        return newDialogueFilePath;
    }

    public void setNewDialogueFilePath(String newDialogueFilePath) {
        this.newDialogueFilePath = newDialogueFilePath;
    }

    public String getQuestGiverName() {
        return questGiverName;
    }

    public void setQuestGiverName(String questGiverName) {
        this.questGiverName = questGiverName;
    }

    public List<Item> getItemToChoose() {
        return itemToChoose;
    }

    public void setItemToChoose(List<Item> itemToChoose) {
        this.itemToChoose = itemToChoose;
    }

    public Spell getSpellToChoose() {
        return spellToChoose;
    }

    public void setSpellToChoose(Spell spellToChoose) {
        this.spellToChoose = spellToChoose;
    }

    public Quest() {
        // method stub for easier gson deserialization
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
        // Check if the objective exists
        if (objectives.containsKey(objectiveId)) {
            Objective objective = objectives.get(objectiveId);
            objective.setIsCompleted(true);
            System.out.println(objective.getCompletionMessage());

            // Mark previous objectives as completed
            markPreviousObjectivesCompleted(objectiveId);

            if (checkProgress()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(completionMessage);
                giveRewards();
                if (isPrimary && !isFinal) {
                    // Base directory for constructing paths
                    String baseDir = System.getProperty("user.dir") + File.separator + "app" + File.separator + "src" + File.separator + "main"
                            + File.separator + "java" + File.separator + "resources" + File.separator + "json";

                    // Construct full paths
                    this.newRegionFilePath = baseDir + File.separator + "regions" + File.separator + newRegionFilePath;
                    this.newAdjacencyFilePath = baseDir + File.separator + "adjacencies" + File.separator
                            + newAdjacencyFilePath;
                    this.newItemsFilePath = baseDir + File.separator + "items" + File.separator + newItemsFilePath;
                    this.newPeopleFilePath = baseDir + File.separator + "people" + File.separator + newPeopleFilePath;
                    this.newDialogueFilePath = baseDir + File.separator + "dialogue" + File.separator
                            + newDialogueFilePath;

                    gameState.updateRegion(newRegionFilePath, newAdjacencyFilePath, newItemsFilePath, newPeopleFilePath,
                            newDialogueFilePath);
                } else if (isFinal) {
                    gameState.credits();
                }
            }
        }
    }

    private void markPreviousObjectivesCompleted(String objectiveId) {
        // Extract the numeric part of the objectiveId
        int targetObjectiveNumber = Integer.parseInt(objectiveId.split("_")[1]);

        // Iterate through all objectives and mark completed objectives before the
        // target objective
        for (Map.Entry<String, Objective> entry : objectives.entrySet()) {
            String currentObjectiveId = entry.getKey();
            // Extract the numeric part of the current objective ID
            int currentObjectiveNumber = Integer.parseInt(currentObjectiveId.split("_")[1]);

            // If the current objective number is less than the target, mark it as completed
            if (currentObjectiveNumber < targetObjectiveNumber) {
                Objective currentObjective = entry.getValue();
                if (!currentObjective.getIsCompleted()) {
                    currentObjective.setIsCompleted(true);
                    System.out.println("Previous objective completed: " + currentObjective.getCompletionMessage());
                }
            }
        }
    }

    public void giveRewards() {
        List<Item> finalList = new ArrayList<>();
        for (Object item : itemToChoose) {
            if (item instanceof Item) {
                finalList.add((Item) item);
            } else if (item instanceof List<?>) {
                for (Object subItem : (List<?>) item) {
                    if (subItem instanceof Item) {
                        finalList.add((Item) subItem);
                    }
                }
            }
        }
        this.itemToChoose = finalList;

        if ("complete".equals(status)) {
            gameState.incrementStartNode(questGiverName);
            if (isPrimary) {
                if (!isFinal) {

                    System.out.println("You receive " + gameState.getPlayer().getNextLevelXp() + " xp");
                    gameState.getPlayer().gainXP(gameState.getPlayer().getNextLevelXp(), gameState);
                    if (gameState.getPlayer().getCharacterClass().getClassName().equalsIgnoreCase("technologist")) {
                        rewardChoice();
                    } else {
                        if (gameState.getPlayer().getRemainingCarryWeight() >= itemToChoose.get(0).getWeight()) {
                            gameState.getPlayer().addItemToInventory(itemToChoose.get(0));
                            System.out.println(itemToChoose.get(0).getName() + " has been added to your inventory.");
                        } else {
                            gameState.safeZoneInventory.addItemToInventory(itemToChoose.get(0));
                            System.out.println(
                                    itemToChoose.get(0).getName() + " has been added to the safe zone inventory.");
                        }
                    }
                }
            } else {
                System.out.println(
                        "You receive " + String.format("%.2f", gameState.getPlayer().getNextLevelXp() / 3) + " xp");
                gameState.getPlayer().gainXP(gameState.getPlayer().getNextLevelXp() / 3, gameState);
            }
            while (this.rewards.get(0) instanceof Iterable<?>) {
                flattenRewards(this.rewards);
            }
            for (Item reward : rewards) {
                System.out.println("You received: " + reward.getName() + " in safe zone inventory.");
                gameState.safeZoneInventory.addItemToInventory(reward);
            }
        }
    }

    public void rewardChoice() {
        System.out.println("You can choose to learn a new spell, or receive the following item: ");
        System.out.println(
                "1. " + spellToChoose.getName() + ", a spell which does " + spellToChoose.getDamage() + " damage");
        System.out.println("2. " + itemToChoose.get(0).getName() + ", a weapon which does "
                + ((Weapon) itemToChoose).getDieQuantity() + "d" + ((Weapon) itemToChoose).getDieFaces() + " damage");

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while (choice != 1 && choice != 2) {
            System.out.print("Enter 1 to choose the spell or 2 to choose the item: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1) {
                    gameState.getPlayer().addSpell(spellToChoose);
                    System.out.println("That spell will now be useable in combat.");
                } else if (choice == 2) {
                    if (gameState.getPlayer().getRemainingCarryWeight() >= itemToChoose.get(0).getWeight()) {
                        gameState.getPlayer().addItemToInventory(itemToChoose.get(0));
                        System.out.println("That weapon has been added to your inventory.");
                    } else {
                        gameState.safeZoneInventory.addItemToInventory(itemToChoose.get(0));
                        System.out.println("That weapon has been added to the safe zone inventory.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // First, convert the base `Quest` object to JSON
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();

        // Add the "type" field to specify the quest type
        jsonObject.addProperty("type", "Quest");

        // Serialize the objectives (assuming the objectives is a Map or similar
        // structure)
        JsonObject objectivesJson = new JsonObject();
        for (Map.Entry<String, Objective> entry : objectives.entrySet()) {
            // Ensure the Objective is serialized properly (use its toSerializableFormat
            // method)
            objectivesJson.add(entry.getKey(), JsonParser.parseString(entry.getValue().toSerializableFormat()));
        }
        jsonObject.add("objectives", objectivesJson);

        // Serialize the rewards
        List<Item> flattenedRewards = new ArrayList<>();
        for (Object reward : rewards) {
            if (reward instanceof List) {
                // Add all items from the inner list to the flattened list
                flattenedRewards.addAll((List<Item>) reward);
            } else if (reward instanceof Item) {
                // Add the single item directly
                flattenedRewards.add((Item) reward);
            } else {
                System.out.println("Unexpected type in rewards: " + reward.getClass().getName());
            }
        }

        // Now use `flattenedRewards` for serialization
        JsonArray rewardsJson = new JsonArray();
        for (Item item : flattenedRewards) {
            rewardsJson.add(JsonParser.parseString(item.toSerializableFormat()));
        }
        jsonObject.add("rewards", rewardsJson);

        // Serialize the itemToChoose (assuming itemToChoose is a List of Item objects)
        List<Item> flattenedItemToChoose = new ArrayList<>();
        for (Object item : itemToChoose) {
            if (item instanceof List) {
                // Add all items from the inner list to the flattened list
                flattenedItemToChoose.addAll((List<Item>) item);
            } else if (item instanceof Item) {
                // Add the single item directly
                flattenedItemToChoose.add((Item) item);
            } else {
                System.out.println("Unexpected type in itemToChoose: " + item.getClass().getName());
            }
        }

        JsonArray itemToChooseJson = new JsonArray();
        for (Item item : flattenedItemToChoose) {
            itemToChooseJson.add(JsonParser.parseString(item.toSerializableFormat()));
        }
        jsonObject.add("itemToChoose", itemToChooseJson);

        // Serialize the spellToChoose (if it exists)
        if (spellToChoose != null) {
            jsonObject.add("spellToChoose", JsonParser.parseString(spellToChoose.toSerializableFormat()));
        }

        // Handle file path fields to record only file names
        jsonObject.addProperty("newRegionFilePath", extractFileName(newRegionFilePath));
        jsonObject.addProperty("newAdjacencyFilePath", extractFileName(newAdjacencyFilePath));
        jsonObject.addProperty("newItemsFilePath", extractFileName(newItemsFilePath));
        jsonObject.addProperty("newPeopleFilePath", extractFileName(newPeopleFilePath));
        jsonObject.addProperty("newDialogueFilePath", extractFileName(newDialogueFilePath));

        // Return the final JSON string
        return gson.toJson(jsonObject);
    }

    // Helper method to extract file name from a full path
    private String extractFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }
        return new File(filePath).getName();
    }
}