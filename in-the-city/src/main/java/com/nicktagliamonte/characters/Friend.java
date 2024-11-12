package com.nicktagliamonte.characters;

import java.util.List;
import java.util.Map;

import com.nicktagliamonte.items.Item;

public class Friend extends NPC {
    private double shrewdness;
    private List<String> hints;
    private List<String> barterSuccessDialogue;
    private List<String> barterFailureDialogue;
    private List<String> questDialogue;
    private boolean canGiveQuest;

    public Friend(String name, double health, double energy, List<Item> inventory, String description, double maxHealth,
                  List<String> dialogue, double shrewdness, List<String> hints, List<String> barterSuccessDialogue, 
                  List<String> barterFailureDialogue, List<String> questDialogue, boolean canGiveQuest) {
        super(name, health, energy, inventory, description, maxHealth, dialogue);
        this.shrewdness = shrewdness;
        this.hints = hints;
        this.barterSuccessDialogue = barterSuccessDialogue;
        this.barterFailureDialogue = barterFailureDialogue;
        this.questDialogue = questDialogue;
        this.canGiveQuest = canGiveQuest;
    }

    //TODO: update all below bartering interaction methods
    public String initiateBarter(int playerCharisma) {
        boolean success = checkBarterSuccess(playerCharisma);
        return respondToBarter(success);
    }

    private boolean checkBarterSuccess(int playerCharisma) {
        return playerCharisma > this.shrewdness * Math.random();
    }

    private String respondToBarter(boolean success) {
        return success ? getBarterDialogue(barterSuccessDialogue) : getBarterDialogue(barterFailureDialogue);
    }

    private String getBarterDialogue(List<String> dialogueList) {
        return dialogueList.get((int) (Math.random() * dialogueList.size()));
    }

    public String talk() {
        return super.getRandomDialogue();
    }

    public String getHint() {
        return hints.isEmpty() ? "No hints available." : hints.get((int) (Math.random() * hints.size()));
    }

    public void giveQuest() {
        if (canGiveQuest) {
            //TODO: figure this out much later, when i'm adding quests.
        }
        System.out.println(questDialogue.get(0));
    }

    public boolean isFriend() {
        return true;
    }
}