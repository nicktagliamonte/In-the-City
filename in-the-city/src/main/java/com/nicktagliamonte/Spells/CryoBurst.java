package com.nicktagliamonte.Spells;

import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Player;

public class CryoBurst extends Spell {
    @Expose private int dieQuantity = 5;
    @Expose private int dieFaces = 8;

    public CryoBurst() {
        super("Cryo Burst", "Unleashes a blast of freezing energy that encases the target in ice.", "5d8");
    }

    @Override
    public void cast(Player caster, Adversary adversary) {
        int baseDamage = new Random().ints(dieQuantity, 1, dieFaces + 1).sum();
        int strengthBonus = (int) (Math.random() * caster.getStrength()) + 1;
        int totalDamage = baseDamage + strengthBonus;

        // Apply hunger penalty if the caster is in hunger
        if (caster.inHunger()) {
            totalDamage = Math.max(totalDamage - caster.getLevel(), 1);
        }

        // Inflict damage on the adversary
        adversary.takeDamage(totalDamage);

        // Notify the user
        pauseForEffect();
        System.out.println(caster.getName() + " casts " + getName() + " and deals " 
            + totalDamage + " damage to " + adversary.getName() + "!");
    }

    // Helper method to handle the delay
    private void pauseForEffect() {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "CryoBurst");
        return gson.toJson(jsonObject);
    }
}