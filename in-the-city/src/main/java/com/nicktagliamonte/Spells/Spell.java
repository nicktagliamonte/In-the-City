package com.nicktagliamonte.Spells;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Player;

// Spells are learned the same way weapons are gained: purchase, acheivement, or discovery
public abstract class Spell {
    @Expose private String name;
    @Expose private String description;
    @Expose private String damage;

    public Spell(String name, String description, String damage) {
        this.name = name;
        this.description = description;
        this.damage = damage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Abstract method for the spell's effect
    public abstract void cast(Player caster, Adversary adversary);

    public abstract String toSerializableFormat();
}