package com.nicktagliamonte.items;

import com.nicktagliamonte.characters.Player;

public class Weapon extends Item {
    private int acBonus;
    private boolean isEquipped;

    public Weapon(String name, String description, double weight, int value, int acBonus) {
        super(name, description, weight, false, value);
        this.acBonus = acBonus;
        isEquipped = false;
    }

    public void equip(Player player) {
        if (player.weapon != null) {
            System.out.println("You can only weild 1 weapon item at a time. Dequip the current weapon before equipping this one.");
        }
        if (!isEquipped) {
            isEquipped = true;
            player.setWeapon(this);
            System.out.println("Successfully equipped " + super.getName());
        }
        
    }

    public void dequip(Player player) {
        if (isEquipped) {
            isEquipped = false;
            player.addItem(this);
            player.removeWeapon();
            System.out.println("Successfully dequipped " + super.getName());
        } else {
            System.out.println("That weapon is not currently equipped");
        }
    }
}