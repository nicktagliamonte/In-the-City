package com.nicktagliamonte.items;

import java.util.*;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.game.GameState;

public class SmallTrap extends Trap {
    @Expose private static final int DURATION = 600;
    @Expose private static final int INTERVAL = 1000;
    @Expose private static final double SUCCESS_RATE = 0.75;
    @Expose private int elapsedTime = 0;
    @Expose private boolean success;
    @Expose private Random random = new Random();
    GameState gameState;

    public SmallTrap(GameState gameState) {
        super("Small Trap", "A small trap. Good for finding simple supplies.", 1, true, 1, gameState);
        Map<Item, Integer> cost = new HashMap<Item, Integer>();
        //TODO: in codebase creation, once i have a scrap item created, update this to cost 1 scrap and make the generated items real
        cost.put(new FuelCell(), 1);
        super.setCost(cost);
        this.gameState = gameState;
    }

    @SuppressWarnings("unused")
    public void springTrap() {
        if (success) {
            List<Item> result = new ArrayList<>();

            int numberOfItems = getWeightedNumberOfItems();

            for (int i = 0; i < numberOfItems; i++) {
                double roll = random.nextDouble();
                Item item;

                if (roll < 0.10) {
                    //TODO: medical
                } else if (roll < 0.30) { 
                    //TODO: food
                } else if (roll < 0.65) { 
                    //TODO: scrap
                } else {
                    //TODO: water
                }

                //TODO: result.add(item); (these are todos because i don't have class files for medical/food/scrap/water yet)
                //TODO: super.springTrap(result); <- this is where the method to add the items to the room inventory live
            }
        } else {
            expire();
        }
    }

    public int getWeightedNumberOfItems() {
        double[] probabilities = {0.20, 0.60, 0.80, 0.90, 0.95, 1.00};

        double roll = random.nextDouble();

        for (int i = 0; i < probabilities.length; i++) {
            if (roll < probabilities[i]) {
                return i + 3;
            }
        }
        return 7;
    }

    @Override
    public void use(GameState gameState) {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("You set a small trap.  Hopefully it catches something.");
        super.setTrap(gameState.getCurrentRoom().getPlayerPosition());
        success = random.nextDouble() < SUCCESS_RATE;
        elapsedTime = 0;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTime++;
                if (elapsedTime >= DURATION) {
                    timer.cancel();
                    expire();
                }
                springTrap();
            }
        }, 0, INTERVAL);
    }
}
