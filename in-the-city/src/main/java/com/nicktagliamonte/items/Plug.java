package com.nicktagliamonte.items;

import java.util.List;
import java.util.Collection;

import com.nicktagliamonte.game.GameState;
import com.nicktagliamonte.quests.Quest;

public class Plug extends Item {
    public Plug(String name, String description, double weight, boolean isConsumable, double value) {
        super(name, description, weight, isConsumable, value, "", false);
    }

    @Override
    public void use(GameState gameState) {
        List<Quest> activeQuests = gameState.getPlayer().getActiveQuests();
        for (Quest quest : activeQuests) {
            if (quest.getQuestId().equalsIgnoreCase("quest_003")) {
                super.setInteractable(true);
            }
        }
        
        if (super.getInteractable()) {
            super.setDescription("A power cord attached to the computer terminal, which has been plugged in.");
            System.out.println("The computer terminal is plugged in.");
            Collection<Item> items = gameState.getCurrentRoom().getItemsInRoom().values();
            for (Item item : items) {
                if (item.getName().equalsIgnoreCase("computer terminal")) {
                    item.setInteractable(true);
                }
            }
        } else {
            System.out.println("There is no power to the area yet. You leave the plug on the ground. Maybe there's someone you can talk to about this.");
        }        
    }
}
