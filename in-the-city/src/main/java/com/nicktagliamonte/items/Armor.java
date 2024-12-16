package com.nicktagliamonte.items;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Player;
import com.nicktagliamonte.game.GameState;

public class Armor extends Item {
    @Expose private int acBonus;
    @Expose private boolean isEquipped;

    public Armor(String name, String description, double weight, int value, int acBonus) {
        super(name, description, weight, false, value, "");
        this.acBonus = acBonus;
        isEquipped = false;
    }

    public void equip(Player player) {
        if (player.armor != null) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You can only wear 1 armor item at a time. Dequip the current armor before equipping this one.");
        }
        if (!isEquipped) {
            player.increaseAC(acBonus);
            isEquipped = true;
            player.setArmor(this);
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
            player.increaseAC(-(acBonus));
            isEquipped = false;
            player.addItem(this);
            player.removeArmor();
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
            System.out.println("That armor item is not currently equipped");
        }
    }

    @Override
    public void use(GameState gameState) {
        if (gameState.getPlayer().armor != null) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You can only wear 1 armor item at a time. Dequip the current armor before equipping this one.");
        }
        if (!isEquipped) {
            gameState.getPlayer().increaseAC(acBonus);
            isEquipped = true;
            gameState.getPlayer().setArmor(this);
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Successfully equipped " + super.getName());
        }  
    }
}