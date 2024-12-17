package com.nicktagliamonte.characters;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.items.Item;

public class PartyMember extends NPC{
    @Expose private CharacterClass characterClass;
    @Expose private double maxCarryWeight;
    @Expose private double damage;
    @Expose private double maxHealth;

    public PartyMember(String name, List<Item> inventory, String description, double damage, CharacterClass characterClass) {
        super(name, characterClass.getHealth(), inventory, description, characterClass.getHealth(),
                characterClass.getStrength(), characterClass.getDexterity(), characterClass.getConstitution(), characterClass.getIntelligence(), 
                characterClass.getWisdom(), characterClass.getCharisma(), characterClass.getAc(), 0);
        this.damage = damage;
        this.characterClass = characterClass;
        this.maxCarryWeight = characterClass.getMaxCarryWeight();
        this.maxHealth = characterClass.getHealth();
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public String getClassName() {
        return characterClass.getClassName();
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    public void setMaxCarryWeight(double maxCarryWeight) {
        this.maxCarryWeight = maxCarryWeight;
    }

    public double getMaxCarryWeight() {
        return this.maxCarryWeight;
    }

    public void listInventory() {
        List<Item> inventory = super.getInventory();
        if (inventory.isEmpty()) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("No items in inventory");
        } else {
            Map<String, Long> itemCount = inventory.stream()
                    .collect(Collectors.groupingBy(Item::getName, Collectors.counting()));

            // Print items with their counts, formatted
            itemCount.forEach((name, count) -> 
                System.out.printf("%-15s %d%n", name, count));
        }
    }
}
