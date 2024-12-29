package com.nicktagliamonte.items;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.game.GameState;

public class BigMagicalTrap extends Trap {
    @Expose private static final int DURATION = 83;
    @Expose private static final int INTERVAL = 1000;
    @Expose private static final double SUCCESS_RATE = 0.90;
    @Expose private int elapsedTime = 0;
    @Expose private boolean success;
    @Expose private transient Random random = new Random();
    public transient GameState gameState;

    public BigMagicalTrap(GameState gameState) {
        super("Big Magical Trap", "A big trap. Good for finding supplies.", 8, true, 8, gameState);
        Map<Item, Integer> cost = new HashMap<Item, Integer>();
        cost.put(new Scrap(), 2);
        cost.put(new FoodRation(), 1);
        cost.put(new FuelCell(), 1);
        super.setCost(cost);
        this.gameState = gameState;
    }

    public BigMagicalTrap() {
        super("Big Magical Trap", "A big trap. Good for finding supplies.", 8, true, 8);
        Map<Item, Integer> cost = new HashMap<Item, Integer>();
        cost.put(new Scrap(), 2);
        cost.put(new FoodRation(), 1);
        cost.put(new FuelCell(), 1);
        super.setCost(cost);
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void springTrap() {
        if (success) {
            List<Item> result = new ArrayList<>();

            int numberOfItems = getWeightedNumberOfItems();

            for (int i = 0; i < numberOfItems; i++) {
                double roll = random.nextDouble();
                Item item;

                if (roll < 0.20) {  // 20% chance for Scrap, Water, or FoodRation (lower chance)
                    int itemChoice = random.nextInt(3);  // Randomly pick one from Scrap, Water, or FoodRation
                    if (itemChoice == 0) {
                        item = new Scrap();
                    } else if (itemChoice == 1) {
                        item = new Water();
                    } else {
                        item = new FoodRation();
                    }
                } else if (roll < 0.50) {  // 30% chance for Medical (moderate chance)
                    item = new Medical();
                } else if (roll < 0.80) {  // 30% chance for FuelCell (moderate chance)
                    item = new FuelCell();
                } else {  // 20% chance for Relic (lower chance)
                    item = new Relic();
                }

                result.add(item);
            }

            super.springTrap(result);
        } else {
            expire();
        }
    }

    public int getWeightedNumberOfItems() {
        double[] probabilities = {0.10, 0.50, 0.70, 0.90, 0.95, 1.00};

        double roll = random.nextDouble();

        for (int i = 0; i < probabilities.length; i++) {
            if (roll < probabilities[i]) {
                return i + 5;  // This increments the minimum and maximum range by 2 (starts from 5 items)
            }
        }
        return 9;  // Default value (increased by 2)
    }

    @Override
    public void use(GameState gameState) {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("You set a big trap. Hopefully it catches something.");
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
                    if (success) {
                        springTrap(); // Only spring the trap after the duration if successful
                    } else {
                        System.out.println("The trap didn't catch anything.");
                    }
                    expire();
                }
            }
        }, 0, INTERVAL);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        super.gameState = gameState;
    }

    @Override
    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();

        jsonObject.addProperty("type", "BigMagicalTrap");

        JsonArray serializedCost = new JsonArray();
        if (super.getCost() != null) {
            for (Map.Entry<Item, Integer> entry : super.getCost().entrySet()) {
                JsonObject costItem = new JsonObject();
                costItem.add("item", JsonParser.parseString(entry.getKey().toSerializableFormat()));
                costItem.addProperty("quantity", entry.getValue());
                serializedCost.add(costItem);
            }
        }

        jsonObject.add("cost", serializedCost);

        return gson.toJson(jsonObject);
    }
}