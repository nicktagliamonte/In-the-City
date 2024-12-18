package com.nicktagliamonte.Spells;

import java.util.Random;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Player;

public class IonSurge extends Spell {
    @Expose private int dieQuantity = 1;
    @Expose private int dieFaces = 6;

    public IonSurge() {
        super("Ion Surge", "Releases a wave of ionized particles that zaps enemies within a short range.", "1d6");
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