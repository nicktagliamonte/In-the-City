package com.nicktagliamonte.game;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Conveyance;
import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.PartyMember;
import com.nicktagliamonte.characters.Player;

public class GameTimer {

    @Expose private boolean paused = false;
    @Expose private int elapsedTime = 0;
    @Expose private final int interval = 1000;
    @Expose private transient GameState gameState;
    @Expose private transient RandomEventManager randomEventManager;
    @Expose private transient Timer timer;

    public GameTimer(GameState gameState) {
        timer = new Timer(true); // Daemon thread, ends when program ends
        this.gameState = gameState;
        randomEventManager = new RandomEventManager(gameState);
        startTimer();
    }

    private void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!paused) {
                    elapsedTime++;
                    if (elapsedTime % 100 == 0) {
                        if (!gameState.getPlayer().inThirst()) {
                            regenerateHealth();
                        } else {
                            decrementHealth();
                        }
                    }
                }
            }
        }, 0, interval);
    }

    public void pause() {
        if (!paused) {
            paused = true;
        }
    }

    public void resume() {
        if (paused) {
            paused = false;
        }
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void stopTimer() {
        timer.cancel();
        sleep();  // Replaced Thread.sleep(15) with sleep() helper method
        System.out.println("Timer stopped.");
    }

    public void regenerateHealth() {
        Player player = null;
        if (gameState.getPlayer() != null) {
            player = gameState.getPlayer();   
        }
        List<NPC> partyMembers = gameState.getCurrentParty();

        if (canRegenerateHealth(player)) {
            player.setHealth(player.getHealth() + 1);
        }

        for (NPC member : partyMembers) {
            if (canRegenerateHealth(member)) {
                member.setHealth(member.getHealth() + 1);
            }
        }
    }

    private boolean canRegenerateHealth(Object character) {
        if (character instanceof Player) {
            Player p = (Player) character;
            return p.inThirst() && (p.getHealth() < p.getMaxHealth());
        } else if (character instanceof PartyMember) {
            PartyMember pm = (PartyMember) character;
            return pm.getHealth() < pm.getMaxHealth();
        }
        return false;
    }

    public void decrementHealth() {
        gameState.getPlayer().setHealth(gameState.getPlayer().getHealth() - 1);
    }

    public void checkForEvent() {
        randomEventManager.checkForEvent();
    }

    public void checkForHunger() {
        int time = gameState.getPlayer().timeSinceFood++;
        if (time == 2000) {
            gameState.getPlayer().setStatus("Hunger");
            sleep();
            System.out.println("You have entered a hunger state. Combat ability will be impacted until you eat something.");
        }
    }

    public void checkForThirst() {
        int time = gameState.getPlayer().timeSinceWater++;
        if (time == 1000) {
            gameState.getPlayer().setStatus("Thirst");
            sleep();  
            System.out.println("You have entered a thirst state. You'll experience slow health drain until you drink something, or die.");
        }
    }

    public void checkForConveyanceHunger() {
        for (NPC npc : gameState.getCurrentParty()) {
            if (npc instanceof Conveyance) {
                int time = ((Conveyance) npc).timeSinceFuel++;
                if (time == 1000) {
                    ((Conveyance) npc).setInHunger(true);
                    sleep();  
                    System.out.println("Your conveyance needs fuel. You will not have access to their inventory until you use a Fuel Cell.");
                }
            }
        }
    }

    // Helper method for sleep functionality
    private void sleep() {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .setPrettyPrinting()
                        .create();
        return gson.toJson(this);
    }
}