package com.nicktagliamonte.characters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nicktagliamonte.items.FoodRation;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.items.Medical;
import com.nicktagliamonte.items.Scrap;
import com.nicktagliamonte.items.Water;

public class TheCityStreetFriend extends Friend {
    public TheCityStreetFriend() {
        super("buddy", 15, new ArrayList<>(), "A friendly face", 15, null, true, false);

        int randomInt = 5 + (int)(Math.random() * 11);
        Random random = new Random();
        
        List<Item> inventory = new ArrayList<Item>();

        for (int i = 0; i < randomInt; i++) {
            double roll = random.nextDouble();

                if (roll < 0.10) {
                    inventory.add(new Medical());
                } else if (roll < 0.30) { 
                    inventory.add(new FoodRation());
                } else if (roll < 0.65) { 
                    inventory.add(new Scrap());
                } else {
                    inventory.add(new Water());
                }
        }

        super.setInventory(inventory);
    }
}