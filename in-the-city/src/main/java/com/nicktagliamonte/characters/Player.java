package com.nicktagliamonte.characters;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nicktagliamonte.items.Armor;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.items.Trap;
import com.nicktagliamonte.items.Weapon;

public class Player extends Person {
    private CharacterClass characterClass;
    private double strength;
    private double dexterity;
    private double constitution;
    private double intelligence;
    private double wisdom;
    private double charisma;
    private double maxCarryWeight;
    private double remainingCarryWeight;
    private double ac;
    public Armor armor;
    public Weapon weapon;

    public Player(String name, CharacterClass characterClass) {
        super(name);
        this.characterClass = characterClass;
        this.strength = characterClass.getStrength();
        this.dexterity = characterClass.getDexterity();
        this.constitution = characterClass.getConstitution();
        this.intelligence = characterClass.getIntelligence();
        this.wisdom = characterClass.getWisdom();
        this.charisma = characterClass.getCharisma();
        this.maxCarryWeight = characterClass.getMaxCarryWeight();
        this.remainingCarryWeight = this.maxCarryWeight;
        super.setEnergy(characterClass.getEnergy());
        super.setHealth(characterClass.getHealth());
        this.ac = 10 + dexterity;
    }

    public void removeArmor() {
        armor = null;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    public Armor getArmor() {
        return armor;
    }

    public void removeWeapon() {
        weapon = null;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public double getAc() {
        return ac;
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

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getDexterity() {
        return dexterity;
    }

    public void setDexterity(double dexterity) {
        this.dexterity = dexterity;
    }

    public double getConstitution() {
        return constitution;
    }

    public void setConstitution(double constitution) {
        this.constitution = constitution;
    }

    public double getintelligence() {
        return intelligence;
    }

    public void setintelligence(double intelligence) {
        this.intelligence = intelligence;
    }

    public double getWisdom() {
        return wisdom;
    }

    public void setWisdom(double wisdom) {
        this.wisdom = wisdom;
    }

    public double getCharisma() {
        return charisma;
    }

    public void setCharisma(double charisma) {
        this.charisma = charisma;
    }

    public double getMaxCarryWeight() {
        return maxCarryWeight;
    }

    public void setMaxCarryWeight(double maxCarryWeight) {
        this.maxCarryWeight = maxCarryWeight;
    }

    public double getRemainingCarryWeight() {
        return remainingCarryWeight;
    }

    public void reduceRemainingCarryWeight(double input) {
        this.remainingCarryWeight -= input;
    }

    public void increaseRemainingCarryWeight(double input) {
        this.remainingCarryWeight += input;
    }

    public void listInventory() {
        List<Item> inventory = super.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("No items in inventory");
        } else {
            inventory.forEach(item -> System.out.println(item.getName()));
        }
    }

    public void hide() {
        //TODO: unsure of exact gameplay elements here, i think this will require feedback from gamestate
        //like the success chance depends on the adversaries in the region
        System.out.println("working");
    }

    public void useHint() {
        //TODO: gonna need to add a field and a setter for hints
        //in this method, there will be a check on the hints available 
        //then display hint and alter the counter
    }

    public Item getItemFromInventory(String itemName) {
        List<Item> inventory = super.getInventory();
        if (inventory == null) {
            System.out.println("No items in inventory");
        } else {
            for (Item item : inventory) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    if (item.getIsConsumable()) {
                        super.inventory.remove(item);
                        increaseRemainingCarryWeight(item.getWeight());
                    }
                    return item;
                }
            }
        }
        return null;
    }

    public void craftItem(Trap trap) {
        Map<Item, Integer> cost = trap.getCost();
        for (Map.Entry<Item, Integer> entry : cost.entrySet()) {
            Item item = entry.getKey();
            int requiredCount = entry.getValue();

            long countInInventory = inventory.stream()
                .filter(i -> i.getName().equals(item.getName()))
                .count();

            if (countInInventory >= requiredCount) {
                for (int i = 0; i < requiredCount; i++) {
                    int removedCount = 0;
                    Iterator<Item> iterator = inventory.iterator();
                    while (iterator.hasNext() && removedCount < requiredCount) {
                        if (iterator.next().getName().equals(item.getName())) {
                            iterator.remove();
                            removedCount++;
                        }
                    }
                inventory.add(trap);
                System.out.println("Successfully crafted " + trap.getName());
                } 
            } else {
                System.out.println("Crafting failed: insufficient items in inventory.");
            }
        }
    }

    public void increaseAC(int acBonus) {
        this.ac += acBonus;
    }

    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }
}