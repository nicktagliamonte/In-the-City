package com.nicktagliamonte.characters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.gson.annotations.Expose;
import com.nicktagliamonte.Spells.Spell;
import com.nicktagliamonte.game.GameState;
import com.nicktagliamonte.items.Armor;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.items.Trap;
import com.nicktagliamonte.items.Weapon;
import com.nicktagliamonte.quests.Objective;
import com.nicktagliamonte.quests.Quest;
import com.nicktagliamonte.rooms.Adjacency;

public class Player extends Person {
    @Expose private CharacterClass characterClass;
    @Expose private double strength;
    @Expose private double dexterity;
    @Expose private double constitution;
    @Expose private double Intelligence;
    @Expose private double wisdom;
    @Expose private double charisma;
    @Expose private double maxCarryWeight;
    @Expose private double remainingCarryWeight;
    @Expose private double ac;
    @Expose private double maxHealth;
    public Armor armor;
    public Weapon weapon;
    public boolean hasWeapon;
    @Expose private List<Spell> spellbook;
    @Expose private int deathSavingThrows;
    @Expose private String status;
    public int timeSinceFood = 0; //TODO: once there is a food item, have this reset to 0 when food is eaten and remove substring hunger from player status
    public int timeSinceWater = 0; //TODO: see above, but for water
    @Expose private double alignment;
    @Expose private boolean isHiding;
    public int level;
    public double currentXP;
    public double nextLevelXP;
    public double nextLevelXPReward;
    @Expose private List<Quest> questLog;

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
        this.isHiding = false;
        level = 1;
        currentXP = 0;
        nextLevelXP = 100;
        nextLevelXPReward = 1;
        questLog = new ArrayList<>();
    }

    public void addQuest(Quest quest) {
        questLog.add(quest);
    }

    public List<Quest> getAllQuests() {
        return questLog;
    }

    public int getLevel() {
        return level;
    }

    public void setIsHiding(boolean isHiding) {
        this.isHiding = isHiding;
    }

    public boolean getIsHiding() {
        return isHiding;
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
        if (isHiding) {
            return strength - (strength * .2);
        }
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getDexterity() {
        if (isHiding) {
            return dexterity - (dexterity * .05);
        }
        return dexterity;
    }

    public void setDexterity(double dexterity) {
        this.dexterity = dexterity;
    }

    public double getConstitution() {
        if (isHiding) {
            return constitution - (constitution * .1);
        }
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
        if (isHiding) {
            return wisdom + (wisdom * .15);
        }
        return wisdom;
    }

    public void setWisdom(double wisdom) {
        this.wisdom = wisdom;
    }

    public double getCharisma() {
        if (isHiding) {
            return charisma + (charisma * .1);
        }
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
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("No items in inventory.");
        } else {
            // Count occurrences of each item name
            Map<String, Long> itemCount = inventory.stream()
                    .collect(Collectors.groupingBy(Item::getName, Collectors.counting()));

            // Print items with their counts, formatted
            itemCount.forEach((name, count) -> 
                System.out.printf("%-15s %d%n", name, count));
        }
    }

    public Item getItemFromInventory(String itemName) {
        List<Item> inventory = super.getInventory();
        if (inventory == null) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
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
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Successfully crafted " + trap.getName());
                } 
            } else {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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

    public void hasKey(List<Adjacency> adjacencies) {
        for (Adjacency adj : adjacencies) {
            for (Item item : inventory) {
                if (item.getName().equalsIgnoreCase(adj.getAdjoiningRoomName())) {
                    adj.setIsLocked(false);
                }
            }
        }
    }

    public boolean isAlive() {
        return (getHealth() > -(getMaxHealth()));
    }

    public int getAttackModifier() {
        if (!hasWeapon) {
            return level;
        } else {
            return weapon.getAttackModifier();
        }
    }

    public int rollWeaponDamage() {
        if (!hasWeapon) {
            return (int) (Math.random() * level) + 1;
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
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You are fully dead.");
        } else if (isDown()) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You are down and making death saving throws.");
            makeDeathSavingThrow();
        } else {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You took " + amount + " points of damage. Remaining health: " + getHealth());
        }
    }

    public void makeDeathSavingThrow() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Press enter to make a death saving throw.  11 or higher is a success, 10 or lower is a failure.");
        scanner.nextLine();

        int randomInt = (int) (Math.random() * 20) + 1;

        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("You rolled a " + randomInt + ".");

        if (randomInt <= 10) {
            deathSavingThrows -= 1;
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Failure! You are at " + deathSavingThrows + " on your death saving throws.");
        } else {
            deathSavingThrows += 1;
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Success! You are at " + deathSavingThrows + " on your death saving throws.");
        }

        if (deathSavingThrows == 3) {
            super.setHealth(0);
            deathSavingThrows = 0;
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You are stabilized at 0 health.");
        } else if (deathSavingThrows == -3) {
            super.setHealth(this.maxHealth * -1);
            deathSavingThrows = 0;
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You are fully dead.");
        }
    }

    public void setStatus(String status) {
        this.status.concat(status);
    }

    public boolean inHunger() {
        return status.contains("Hunger");
    }

    public boolean inThirst() {
        return status.contains("Thirst");
    }

    public void calculateNextLevelXP() {
        nextLevelXP *= 1.1;
    }

    public void calculateNextLevelXPReward() {
        nextLevelXPReward *= 1.07;
    }

    public void gainXP(double xpGained, GameState gameState) {
        currentXP += (xpGained * nextLevelXPReward);
    
        while (currentXP >= nextLevelXP) {
            levelUp(gameState);
        }
    }

    private void levelUp(GameState gameState) {
        currentXP -= nextLevelXP;
        level++;
        if (characterClass.getClassName().equalsIgnoreCase("technologist")) {
            incrementTechnologist();
        } else if (characterClass.getClassName().equalsIgnoreCase("survivalist")) {
            incrementSurvivalist();
        } else {
            incrementNegotiator();
        }
        this.setHealth(this.maxHealth);
        this.setStatus("");
        calculateNextLevelXP();
        
        if (!(gameState.getCurrentParty() == null || gameState.getCurrentParty().isEmpty())) {
            for (NPC partyMember : gameState.getCurrentParty()) {
                if (((PartyMember) partyMember).getCharacterClass().getClassName().equalsIgnoreCase("technologist")) {
                    partyMember.setStrength(partyMember.getStrength() + (partyMember.getStrength() * 0.0833));
                    partyMember.setDexterity(partyMember.getDexterity() + (partyMember.getDexterity() * 0.075));
                    partyMember.setConstitution(partyMember.getConstitution() + (partyMember.getConstitution() * 0.0875));
                    partyMember.setIntelligence(partyMember.getIntelligence() + (partyMember.getIntelligence() * 0.1));
                    partyMember.setWisdom(partyMember.getWisdom() + (partyMember.getWisdom() * 0.1));
                    partyMember.setCharisma(partyMember.getCharisma() + (partyMember.getCharisma() * 0.0833));
                    ((PartyMember) partyMember).setMaxHealth(partyMember.getMaxHealth() + (partyMember.getMaxHealth() * 0.1111));
                    ((PartyMember) partyMember).setDamage(((PartyMember) partyMember).getDamage() + (((PartyMember) partyMember).getDamage() * 0.1111));
                    partyMember.setAc(partyMember.getAc() + (partyMember.getAc() * .075));
                } else if (((PartyMember) partyMember).getCharacterClass().getClassName().equalsIgnoreCase("survivalist")) {
                    partyMember.setStrength(partyMember.getStrength() + (partyMember.getStrength() * 0.12));
                    partyMember.setDexterity(partyMember.getDexterity() + (partyMember.getDexterity() * 0.1167));
                    partyMember.setConstitution(partyMember.getConstitution() + (partyMember.getConstitution() * 0.09));
                    partyMember.setIntelligence(partyMember.getIntelligence() + (partyMember.getIntelligence() * 0.0667));
                    partyMember.setWisdom(partyMember.getWisdom() + (partyMember.getWisdom() * 0.0667));
                    partyMember.setCharisma(partyMember.getCharisma() + (partyMember.getCharisma() * 0.075));
                    ((PartyMember) partyMember).setMaxHealth(partyMember.getMaxHealth() + (partyMember.getMaxHealth() * 0.1364));
                    ((PartyMember) partyMember).setDamage(((PartyMember) partyMember).getDamage() + (((PartyMember) partyMember).getDamage() * 0.1364));
                    partyMember.setAc(partyMember.getAc() + (partyMember.getAc() * .1));
                } else {
                    partyMember.setStrength(partyMember.getStrength() + (partyMember.getStrength() * 0.1));
                    partyMember.setDexterity(partyMember.getDexterity() + (partyMember.getDexterity() * 0.075));
                    partyMember.setConstitution(partyMember.getConstitution() + (partyMember.getConstitution() * 0.08));
                    partyMember.setIntelligence(partyMember.getIntelligence() + (partyMember.getIntelligence() * 0.0625));
                    partyMember.setWisdom(partyMember.getWisdom() + (partyMember.getWisdom() * 0.1));
                    partyMember.setCharisma(partyMember.getCharisma() + (partyMember.getCharisma() * 0.125));
                    ((PartyMember) partyMember).setDamage(((PartyMember) partyMember).getDamage() + (((PartyMember) partyMember).getDamage() * 0.125));
                    partyMember.setAc(partyMember.getAc() + (partyMember.getAc() * .05));
                }
                partyMember.setHealth(partyMember.getMaxHealth());
            }
        }
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Congratulations! You've reached level " + level);
    }

    private void incrementTechnologist() {
        strength += strength * 0.0833;
        dexterity += dexterity * 0.075;
        constitution += constitution * 0.0875;
        Intelligence += Intelligence * 0.1;
        wisdom += wisdom * 0.1;
        charisma += charisma * 0.0833;
        maxHealth += maxHealth * 0.1111;
        maxCarryWeight += maxCarryWeight * 0.1154;
        remainingCarryWeight += maxCarryWeight * 0.1154; 
        ac += ac * .075;
    }

    private void incrementSurvivalist() {
        strength += strength * 0.12;
        dexterity += dexterity * 0.1167;
        constitution += constitution * 0.09;
        Intelligence += Intelligence * 0.0667;
        wisdom += wisdom * 0.0667;
        charisma += charisma * 0.075;
        maxHealth += maxHealth * 0.1364;
        maxCarryWeight += maxCarryWeight * 0.1667;
        remainingCarryWeight += maxCarryWeight * 0.1667;
        ac += ac * .1;
    }

    private void incrementNegotiator() {
        strength += strength * 0.1;
        dexterity += dexterity * 0.075;
        constitution += constitution * 0.08;
        Intelligence += Intelligence * 0.0625;
        wisdom += wisdom * 0.1;
        charisma += charisma * 0.1167;
        maxHealth += maxHealth * 0.125;
        maxCarryWeight += maxCarryWeight * 0.1429;
        remainingCarryWeight += maxCarryWeight * 0.1429;
        ac += ac * .05;
    }

    public double getNextLevelXp() {
        return nextLevelXP;
    }

    public List<Quest> getActiveQuests() {
        List<Quest> activeQuests = new ArrayList<>();
        for (Quest quest : questLog) {
            boolean allObjectivesCompleted = true;
            for (Objective objective : quest.getObjectives().values()) {
                if (!objective.getIsCompleted()) {
                    allObjectivesCompleted = false;
                    break;
                }
            }

            if (!allObjectivesCompleted) {
                activeQuests.add(quest);
            }
        }

        return activeQuests;
    }

    public List<Quest> getCompletedQuests() {
        List<Quest> completedQuests = new ArrayList<>();
        for (Quest quest : questLog) {
            boolean allObjectivesCompleted = true;
            for (Objective objective : quest.getObjectives().values()) {
                if (!objective.getIsCompleted()) {
                    allObjectivesCompleted = false;
                    break;
                }
            }

            if (allObjectivesCompleted) {
                completedQuests.add(quest);
            }
        }

        return completedQuests;
    }
}