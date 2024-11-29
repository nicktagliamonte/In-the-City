package com.nicktagliamonte.items;

import com.nicktagliamonte.characters.Player;
import com.nicktagliamonte.game.GameState;

public class Armor extends Item {
    private int acBonus;
    private boolean isEquipped;

    public Armor(String name, String description, double weight, int value, int acBonus) {
        super(name, description, weight, false, value);
        this.acBonus = acBonus;
        isEquipped = false;
    }

    public void equip(Player player) {
        if (player.armor != null) {
            System.out.println("You can only wear 1 armor item at a time. Dequip the current armor before equipping this one.");
        }
        if (!isEquipped) {
            player.increaseAC(acBonus);
            isEquipped = true;
            player.setArmor(this);
            System.out.println("Successfully equipped " + super.getName());
        }        
    }

    public void dequip(Player player) {
        if (isEquipped) {
            player.increaseAC(-(acBonus));
            isEquipped = false;
            player.addItem(this);
            player.removeArmor();
            System.out.println("Successfully dequipped " + super.getName());
        } else {
            System.out.println("That armor item is not currently equipped");
        }
    }

    @Override
    public void use(GameState gameState) {
        if (gameState.getPlayer().armor != null) {
            System.out.println("You can only wear 1 armor item at a time. Dequip the current armor before equipping this one.");
        }
        if (!isEquipped) {
            gameState.getPlayer().increaseAC(acBonus);
            isEquipped = true;
            gameState.getPlayer().setArmor(this);
            System.out.println("Successfully equipped " + super.getName());
        }  
    }
}