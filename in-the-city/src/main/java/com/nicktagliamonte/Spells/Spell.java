package com.nicktagliamonte.Spells;

import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Player;

public abstract class Spell {
    private String name;
    private String description;

    public Spell(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Abstract method for the spell's effect
    public abstract void cast(Player caster, Adversary adversary);
}