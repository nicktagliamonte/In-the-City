package com.nicktagliamonte.Spells;

import java.util.Random;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Player;

public class ThermalLance extends Spell {
    @Expose private int dieQuantity = 1;
    @Expose private int dieFaces = 4;

    public ThermalLance() {
        super("Thermal Lance", "Fires a concentrated beam of heat energy that burns through armor and flesh.", "5d10");
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
}