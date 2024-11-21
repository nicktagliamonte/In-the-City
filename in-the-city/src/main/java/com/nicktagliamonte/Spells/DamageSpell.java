//TODO: replace this with an actual spell
package com.nicktagliamonte.Spells;

import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Player;

public class DamageSpell extends Spell {
    private int damage;

    public DamageSpell(String name, String description, int damage) {
        super(name, description);
        this.damage = damage;
    }

    @Override
    public void cast(Player caster, Adversary adversary) {
        adversary.takeDamage(damage);
        System.out.println(caster.getName() + " casts " + getName() + " and deals " + damage + " damage to " + adversary.getName() + "!");
    }
}