package com.nicktagliamonte.game;

import java.util.Random;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Friend;
import com.nicktagliamonte.characters.TheCityStreetAdversary;
import com.nicktagliamonte.characters.TheCityStreetFriend;
import com.nicktagliamonte.characters.TheDilapidatedBuildingAdversary;
import com.nicktagliamonte.characters.TheDilapidatedBuildingFriend;

public class RandomEventManager {
    @Expose private transient GameState gameState;
    @Expose private transient Random random;

    public RandomEventManager(GameState gameState) {
        this.gameState = gameState;
        this.random = new Random();
    }

    public void checkForEvent() {
        int eventChance = random.nextInt(100);

        if (eventChance < 2) {
            triggerCombatEvent();
        } else if (eventChance < 4 && !(gameState.getPlayer().getInventory().isEmpty())) {
            triggerBarterEvent();
        } else if (eventChance < 15) {
            triggerFlavorTextEvent();
        }
    }

    private void triggerCombatEvent() {
        boolean success = false;
        int chance = random.nextInt(2);
        if (!(gameState.getCurrentRoom().getIsSafe() || gameState.getCurrentRoom().getIsEconomic()) || (gameState.getCurrentRoom().getIsEconomic() && chance == 0)) {
            success = true;
        }

        if (success) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("A desparate scavenger approaches with a knife, wild eyed.");
            //TODO: eventually, this will require a 9-part if/else to get the name of the current region and initialize the appropriate adversary for that region.
            //the regionadversary constructors should also have some kind of randomization function to set the stats to some random number within a range
            //i.e. dead zone health 1-10, liacouras health 50-70
            //or whatever numbers make sense at the time
            Adversary adversary = null;
            if (gameState.getCurrentRegion().getRegionName().equals("The Dilapidated Building")) {
                adversary = new TheDilapidatedBuildingAdversary();
            } else {
                adversary = new TheCityStreetAdversary();
            }
            
            gameState.enterCombat(adversary);
        }
    }

    private void triggerBarterEvent() {
        int chance = random.nextInt(4);
        
        if (gameState.getCurrentRoom().getIsEconomic() || (gameState.getCurrentRoom().getIsSafe() && chance == 0)) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("An wandering traveler offers to trade with you.");
            //TODO: eventually, this will require a 9-part if/else to get the name of the current region and initialize the appropriate friend for that region.
            //the regionFriend constructors should also have some kind of randomization function to set the inventory to some random set within a range
            //i.e. dead zone 1 fuelcell, liacouras 10 swords
            //or whatever items make sense at the time
            Friend friend = null;
            if (gameState.getCurrentRegion().getRegionName().equals("The Dilapidated Building")) {
                friend = new TheDilapidatedBuildingFriend();
            } else {
                friend = new TheCityStreetFriend();
            }
            gameState.enterBarter(friend);
        }
    }

    private void triggerFlavorTextEvent() {
        // Random flavor text event
        //TODO: when it comes to the actual game, the flavor text of a region will be held in the region object. 
        //then we'll get a 9-item if/else to set the String[] here
        String[] flavorText = {
            "The wind picks up, carrying a faint scent of smoke.",
            "You hear a distant rumble.",
            "The shadows grow longer, and you can't shake the feeling of being watched.",
            "The faint scent of burning wood lingers in the air.", 
            "A soft rustling sound comes from behind the cracked door.", 
            "Dust particles swirl in the light, almost like tiny ghosts.", 
            "The air smells stale, as though this place hasn't been used in years.", 
            "A distant bell tolls, its sound muffled by the walls.", 
            "The floor creaks with every step, echoing through the quiet space.", 
            "Something moves in the corner of your vision, but when you look, there's nothing there.", 
            "The cold metal of the railing feels oddly comforting against your palm.", 
            "A chill runs down your spine as you hear a faint, unintelligible whisper.", 
            "The faint taste of salt lingers in the air, as if the sea is close by.", 
            "The walls seem to close in as the light dims.", 
            "The air tastes faintly of iron, leaving a metallic aftertaste.", 
            "A faint glow from an unknown source illuminates the far corner.", 
            "Your footsteps echo unnervingly in the empty hallway.", 
            "A gentle breeze stirs the curtains, though no windows are open.", 
            "The sound of dripping water echoes from somewhere distant.", 
            "Old paintings line the walls, their eyes following you as you move.", 
            "The hum of electricity fills the air, creating a sense of unease.", 
            "Faint laughter echoes, but no one is in sight.", 
            "A cold draft cuts through the room, sending a shiver down your spine.", 
            "The ground beneath you seems to vibrate softly, as if something large is moving below.", 
            "You catch a glimpse of a shadow passing just out of sight.", 
            "The musty smell of old books fills the air, mixed with the scent of mildew.", 
            "The air is thick with tension, as if something is about to happen.", 
            "A faint, melodic tune plays from somewhere unseen, barely audible.", 
            "The walls are lined with cracked tiles, some of which are missing entirely.", 
            "Your breath comes out in a visible cloud, the room cold and uninviting.", 
            "A thick fog seems to settle, obscuring everything beyond arm's reach.", 
            "The floor beneath you groans with each step, protesting your weight.", 
            "Flickering lights cast long, eerie shadows across the room.", 
            "The distant sound of running water grows louder as you move deeper.", 
            "The smell of burnt food lingers in the air, despite no sign of a fire."
        };
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(flavorText[random.nextInt(flavorText.length)]);
    }
}