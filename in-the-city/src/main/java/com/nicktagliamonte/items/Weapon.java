package com.nicktagliamonte.items;

import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Player;

public class Weapon extends Item {
    @Expose private boolean isEquipped;
    @Expose private int attackModifier;
    @Expose private int dieQuantity;
    @Expose private int dieFaces;

    public Weapon(String name, String description, double weight, int value, String dieString) {
        super(name, description, weight, false, value, "", true);
        isEquipped = false;
        String[] dieArray = dieString.split("d");
        this.dieQuantity = Integer.valueOf(dieArray[0]);
        this.dieFaces = Integer.valueOf(dieArray[1]);
    }

    public void equip(Player player) {
        if (player.weapon != null) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You can only weild 1 weapon item at a time. Dequip the current weapon before equipping this one.");
        }
        if (!isEquipped) {
            isEquipped = true;
            player.setWeapon(this);
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Successfully equipped " + super.getName());
        }
        
    }

    public void dequip(Player player) {
        if (isEquipped) {
            isEquipped = false;
            player.addItem(this);
            player.removeWeapon();
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Successfully dequipped " + super.getName());
        } else {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("That weapon is not currently equipped");
        }
    }

    public boolean isEquipped() {
        return isEquipped;
    }

    public void setEquipped(boolean isEquipped) {
        this.isEquipped = isEquipped;
    }

    public int getAttackModifier() {
        return attackModifier;
    }

    public void setAttackModifier(int attackModifier) {
        this.attackModifier = attackModifier;
    }

    public int getDamage() {
        Random random = new Random();
        int totalDamage = 0;

        for (int i = 0; i < dieQuantity; i++) {
            totalDamage += random.nextInt(dieFaces) + 1;
        }

        return totalDamage;
    }

    public int getDieQuantity() {
        return dieQuantity;
    }

    public void setDieQuantity(int dieQuantity) {
        this.dieQuantity = dieQuantity;
    }

    public int getDieFaces() {
        return dieFaces;
    }

    public void setDieFaces(int dieFaces) {
        this.dieFaces = dieFaces;
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "Weapon");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }    
}