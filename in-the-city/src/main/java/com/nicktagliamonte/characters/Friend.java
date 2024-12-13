package com.nicktagliamonte.characters;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class Friend extends NPC {
    @Expose private List<String> questDialogue;
    @Expose private boolean canGiveQuest;

    public Friend(String name, double health, List<Item> inventory, String description, double maxHealth,
                  List<String> questDialogue, boolean canGiveQuest) {
        super(name, health, inventory, description, maxHealth);
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