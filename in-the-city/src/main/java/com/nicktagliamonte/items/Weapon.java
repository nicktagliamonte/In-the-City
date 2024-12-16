package com.nicktagliamonte.items;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Player;

public class Weapon extends Item {
    @Expose private boolean isEquipped;
    @Expose private int attackModifier;
    @Expose private int damage;

    public Weapon(String name, String description, double weight, int value, int attackModifier, int damage) {
        super(name, description, weight, false, value, "");
        isEquipped = false;
        this.attackModifier = attackModifier;
        this.damage = damage;
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

    public int getAttackModifier() {
        return attackModifier;
    }

    public int getDamage() {
        //TODO: make this be a roll based on the damage range of the weapon, so probably change damage type from int to map <quantity of die, number of faces>
        return damage;
    }
}