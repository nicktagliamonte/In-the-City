package com.nicktagliamonte.characters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.nicktagliamonte.Spells.Spell;
import com.nicktagliamonte.items.Armor;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.items.Trap;
import com.nicktagliamonte.items.Weapon;

public class Player extends Person {
    private CharacterClass characterClass;
    private double strength;
    private double dexterity;
    private double constitution;
    private double Intelligence;
    private double wisdom;
    private double charisma;
    private double maxCarryWeight;
    private double remainingCarryWeight;
    private double ac;
    private double maxHealth;
    public Armor armor;
    public Weapon weapon;
    public boolean hasWeapon;
    private List<Spell> spellbook;
    private int deathSavingThrows;
    private String status;
    public int timeSinceFood = 0; //TODO: once there is a food item, have this reset to 0 when food is eaten and remove substring hunger from player status
    public int timeSinceWater = 0; //TODO: see above, but for water
    private double alignment;

    public Player(String name, CharacterClass characterClass) {
        super(name);
        this.characterClass = characterClass;
        this.strength = characterClass.getStrength();
        this.dexterity = characterClass.getDexterity();
        this.constitution = characterClass.getConstitution();
        this.Intelligence = characterClass.getIntelligence();
        this.wisdom = characterClass.getWisdom();
        this.charisma = characterClass.getCharisma();
        this.maxCarryWeight = characterClass.getMaxCarryWeight();
        this.remainingCarryWeight = this.maxCarryWeight;
        super.setHealth(characterClass.getHealth());
        this.ac = 10 + dexterity;
        this.maxHealth = characterClass.getHealth();
        this.spellbook = new ArrayList<>();
        this.deathSavingThrows = 0;
        this.status = " ";
        this.alignment = 0;
    }

    public void addSpell(Spell spell) {
        spellbook.add(spell);
    }

    public List<Spell> getSpellbook() {
        return spellbook;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
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
        hasWeapon = false;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        hasWeapon = true;
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

    public double getIntelligence() {
        return Intelligence;
    }

    public void setIntelligence(double Intelligence) {
        this.Intelligence = Intelligence;
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

    public double getAlignment() {
        return alignment;
    }

    public void adjustAlignment(double delta) {
        alignment += delta;

        if (alignment > 1.0) {
            alignment = 1.0;
        } else if (alignment < -1.0) {
            alignment = -1.0;
        }
    }

    public void setAlignment(double value) {
        if (value > 1.0) {
            alignment = 1.0;
        } else if (value < -1.0) {
            alignment = -1.0;
        } else {
            alignment = value;
        }
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
            System.out.println("No items in inventory.");
        } else {
            inventory.forEach(item -> System.out.println(item.getName()));
        }
    }

    public void hide() {
        //TODO: unsure of exact gameplay elements here, i think this will require feedback from gamestate
        //like the success chance depends on the adversaries in the region
        System.out.println("working");
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

    public boolean isAlive() {
        return (getHealth() > -(getMaxHealth()));
    }

    public int getAttackModifier() {
        if (!hasWeapon) {
            //TODO: have this return the player level
            return 0;
        } else {
            return weapon.getAttackModifier();
        }
    }

    public int rollWeaponDamage() {
        if (!hasWeapon) {
            //TODO: also have this be impacted by player level
            return 1;
        } else {
            return weapon.getDamage();
        }
    }

    public boolean isDown() {
        return ((super.getHealth() > -maxHealth) && (super.getHealth() < 0));
    }

    public void takeDamage(double amount) {
        super.setHealth(super.getHealth() - amount);
        if (!isAlive()) {
            System.out.println("You are fully dead.");
        } else if (isDown()) {
            System.out.println("You are down and making death saving throws.");
            makeDeathSavingThrow();
        } else {
            System.out.println("You took " + amount + " points of damage. Remaining health: " + getHealth());
        }
    }

    public void makeDeathSavingThrow() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        System.out.println("Press enter to make a death saving throw.  11 or higher is a success, 10 or lower is a failure.");
        scanner.nextLine();

        int randomInt = (int) (Math.random() * 20) + 1;

        System.out.println("You rolled a " + randomInt + ".");

        if (randomInt <= 10) {
            deathSavingThrows -= 1;
            System.out.println("Failure! You are at " + deathSavingThrows + " on your death saving throws.");
        } else {
            deathSavingThrows += 1;
            System.out.println("Success! You are at " + deathSavingThrows + " on your death saving throws.");
        }

        if (deathSavingThrows == 3) {
            super.setHealth(0);
            System.out.println("You are stabilized at 0 health.");
        } else if (deathSavingThrows == -3) {
            super.setHealth(this.maxHealth * -1);
            System.out.println("You are fully dead.");
        }
    }

    public void setStatus(String status) {
        this.status.concat(status);
    }

    public boolean inHunger() {
        return status.contains("Hunger");
    }

    public boolean inFear() {
        return status.contains("Fear");
    }

    public boolean inThirst() {
        return status.contains("Thirst");
    }

    public boolean inTerror() {
        return status.contains("Terror");
    }
}