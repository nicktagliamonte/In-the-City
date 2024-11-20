package com.nicktagliamonte.characters;

import java.util.List;

import com.nicktagliamonte.items.Item;

public class Friend extends NPC {
    private double shrewdness;
    private List<String> questDialogue;
    private boolean canGiveQuest;

    public Friend(String name, double health, double energy, List<Item> inventory, String description, double maxHealth,
                  double shrewdness, List<String> hints, List<String> questDialogue, boolean canGiveQuest) {
        super(name, health, energy, inventory, description, maxHealth, hints);
        this.shrewdness = shrewdness;
        this.questDialogue = questDialogue;
        this.canGiveQuest = canGiveQuest;
    }

    public String initiateBarter(int playerCharisma) {
        return checkBarterSuccess(playerCharisma) ? "You have a deal." : "I just can't go that low, sorry.";
    }

    private boolean checkBarterSuccess(int playerCharisma) {
        return playerCharisma > this.shrewdness * Math.random();
    }

    public void giveQuest() {
        if (canGiveQuest) {
            //TODO: figure this out much later, when i'm adding quests.
        }
        System.out.println(questDialogue.get(0));
    }
}