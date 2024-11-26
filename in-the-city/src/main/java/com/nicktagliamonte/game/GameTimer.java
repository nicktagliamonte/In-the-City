package com.nicktagliamonte.game;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.PartyMember;
import com.nicktagliamonte.characters.Player;

public class GameTimer {

    private boolean paused = false;
    private int elapsedTime = 0;
    private final int interval = 1000;
    private GameState gameState;
    private RandomEventManager randomEventManager;

    private Timer timer;

    public GameTimer() {
        timer = new Timer(true); // Daemon thread, ends when program ends
        startTimer();
    }

    private void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!paused) {
                    elapsedTime++;
                    regenerateHealth();
                    // Call additional event handlers here, e.g., health regen
                }
            }
        }, 0, interval);
    }

    public void pause() {
        paused = true;
        System.out.println("Timer paused.");
    }

    public void resume() {
        paused = false;
        System.out.println("Timer resumed.");
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void stopTimer() {
        timer.cancel();
        System.out.println("Timer stopped.");
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setRandomEventManager(RandomEventManager randomEventManager) {
        this.randomEventManager = randomEventManager;
    }

    public void regenerateHealth() {
        Player player = gameState.getPlayer();
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
            return !p.inHunger() && !p.inFear() && (p.getHealth() < p.getMaxHealth());
        } else if (character instanceof PartyMember) {
            PartyMember pm = (PartyMember) character;
            return pm.getHealth() < pm.getMaxHealth();
        }
        return false;
    }
}