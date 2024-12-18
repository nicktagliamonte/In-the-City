package com.nicktagliamonte.Spells;

import java.util.Random;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Player;

public class PlasmaBolt extends Spell {
    @Expose private int dieQuantity = 1;
    @Expose private int dieFaces = 4;

    public PlasmaBolt() {
        super("Plasma Bolt", "Launches a concentrated bolt of superheated plasma that explodes on impact.", "1d6");
    }

    @Override
    public void cast(Player caster, Adversary adversary) {
        Random random = new Random();
        int totalDamage = 0;

        for (int i = 0; i < dieQuantity; i++) {
            totalDamage += random.nextInt(dieFaces) + 1;
        }

        totalDamage += (int) (Math.random() * caster.getStrength()) + 1;

        if (caster.inHunger()) {
            int damageModified = totalDamage - caster.getLevel();
            if (damageModified < 1) {
                damageModified = 1;
            }
            adversary.takeDamage(damageModified);
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(caster.getName() + " casts " + getName() + " and deals " + (damageModified) + " damage to " + adversary.getName() + "!");
        } else {
            adversary.takeDamage(totalDamage);
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(caster.getName() + " casts " + getName() + " and deals " + totalDamage + " damage to " + adversary.getName() + "!");
        }
    }
}