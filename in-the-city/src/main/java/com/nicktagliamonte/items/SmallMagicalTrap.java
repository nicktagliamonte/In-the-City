package com.nicktagliamonte.items;

import java.util.*;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.game.GameState;

public class SmallMagicalTrap extends Trap {
    @Expose private static final int DURATION = 1000;
    @Expose private static final int INTERVAL = 1000;
    @Expose private static final double SUCCESS_RATE = 0.85;
    @Expose private int elapsedTime = 0;
    @Expose private boolean success;
    @Expose private Random random = new Random();
    GameState gameState;

    public SmallMagicalTrap(GameState gameState) {
        super("Small Trap", "A small trap. Good for finding simple supplies.", 1, true, 1, gameState);
        Map<Item, Integer> cost = new HashMap<Item, Integer>();
        cost.put(new Scrap(), 1);
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
                    result.add(new Medical());
                } else if (roll < 0.30) { 
                    result.add(new FoodRation());
                } else if (roll < 0.65) { 
                    result.add(new Scrap());
                } else {
                    result.add(new Water());
                }

                super.springTrap(result);
            }
        } else {
            expire();
        }
    }

    public int getWeightedNumberOfItems() {
        double[] probabilities = {0.10, 0.50, 0.70, 0.90, 0.95, 1.00};

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
        System.out.println("You set a small magical trap.  Hopefully it catches something.");
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
