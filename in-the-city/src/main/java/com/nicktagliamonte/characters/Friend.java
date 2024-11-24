package com.nicktagliamonte.characters;

import java.util.List;

import com.nicktagliamonte.items.Item;

public class Friend extends NPC {
    private List<String> questDialogue;
    private boolean canGiveQuest;

    public Friend(String name, double health, List<Item> inventory, String description, double maxHealth,
                  List<String> hints, List<String> questDialogue, boolean canGiveQuest) {
        super(name, health, inventory, description, maxHealth, hints);
        this.questDialogue = questDialogue;
        this.canGiveQuest = canGiveQuest;
    }

    public void giveQuest() {
        if (canGiveQuest) {
            //TODO: figure this out much later, when i'm adding quests.
        }
        System.out.println(questDialogue.get(0));
    }
}