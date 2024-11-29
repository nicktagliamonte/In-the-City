package com.nicktagliamonte.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Friend;
import com.nicktagliamonte.characters.TestRegionAdversary;
import com.nicktagliamonte.items.FuelCell;
import com.nicktagliamonte.items.Item;

public class RandomEventManager {
    private GameState gameState;
    private Random random;

    public RandomEventManager(GameState gameState) {
        this.gameState = gameState;
        this.random = new Random();
    }

    public void checkForEvent() {
        // Event frequency (adjust based on zones or other factors)
        int eventChance = random.nextInt(100); // Random number between 0-99

        if (eventChance < 2) {
            triggerCombatEvent();
        } else if (eventChance < 4 && !(gameState.getPlayer().getInventory().isEmpty())) {
            triggerBarterEvent();
        } else if (eventChance < 8) {
            triggerFlavorTextEvent();
        }
    }

    private void triggerCombatEvent() {
        // Check if the player is in a region where combat could happen
        /**
         * TODO: add something to only show half or a third of combat events in economic zone 
         * if (ineconomiczone) {
         *      rand chance = random.nextint(3);
         *      if (chance == 0) {
         *          //whatever to initiate combat
         *      } else {
         *          //do nothing
         *      }
         * }
         */
        //
        if (!gameState.getCurrentRoom().getIsSafe()) {
            System.out.println("A wild adversary approaches! Get ready to fight!");
            //TODO: eventually, this will require a 9-part if/else to get the name of the current region and initialize the appropriate adversary for that region.
            //TODO: the regionadversary constructors should also have some kind of randomization function to set the stats to some random number within a range
            //i.e. dead zone health 1-10, liacouras health 50-70
            //or whatever numbers make sense at the time
            Adversary adversary = new TestRegionAdversary();
            gameState.enterCombat(adversary);
        }
    }

    private void triggerBarterEvent() {
        int chance = random.nextInt(4);
        //TODO: update condition to ineconomiczone || (insafezone && chance == 0)
        if (gameState.getCurrentRoom().getIsSafe() && chance == 0) {
            System.out.println("An NPC offers to trade with you.");
            //TODO: eventually, this will require a 9-part if/else to get the name of the current region and initialize the appropriate friend for that region.
            //TODO: the regionFriend constructors should also have some kind of randomization function to set the inventory to some random set within a range
            //i.e. dead zone 1 fuelcell, liacouras 10 swords
            //or whatever items make sense at the time
            List<Item> inventory = new ArrayList<>();
            inventory.add(new FuelCell());
            Friend friend = new Friend("null", 0, inventory, "a friend", 0, null, false);
            gameState.enterBarter(friend);
        }
    }

    private void triggerFlavorTextEvent() {
        // Random flavor text event
        //TODO: when it comes to the actual game, the flavor text of a region will be held in the region object. 
        //TODO: then we'll get a 9-item if/else to set the String[] here
        String[] flavorText = {
            "The wind picks up, carrying a faint scent of smoke.",
            "You hear a distant rumble, as if the ground itself is warning you.",
            "The shadows grow longer, and you can't shake the feeling of being watched."
        };
        System.out.println(flavorText[random.nextInt(flavorText.length)]);
    }
}