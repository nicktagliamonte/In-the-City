package com.nicktagliamonte.characters;

import java.util.List;

import com.nicktagliamonte.items.Item;

//TODO: on creation of the combat system, the question "do i make party members have equipable items" came up
//I decided against it -- this is not a combat based game, and that would require the person playing to put far too much effort into equipping for combat.
//probably it would be better to have it but that's also so so so much code to write. so this character has flat damage and i'll need to think about this later
public class PartyMember extends NPC{
    private CharacterClass characterClass;
    private double maxCarryWeight;
    private double damage;

    public PartyMember(String name, List<Item> inventory, String description, double damage, CharacterClass characterClass) {
        super(name, characterClass.getHealth(), inventory, description, characterClass.getHealth(),
                characterClass.getStrength(), characterClass.getDexterity(), characterClass.getConstitution(), characterClass.getIntelligence(), 
                characterClass.getWisdom(), characterClass.getCharisma(), characterClass.getAc(), 0);
        this.damage = damage;
        this.characterClass = characterClass;
        this.maxCarryWeight = characterClass.getMaxCarryWeight();
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
            System.out.println("No items in inventory");
        } else {
            inventory.forEach(item -> System.out.println(item.getName()));
        }
    }
}
