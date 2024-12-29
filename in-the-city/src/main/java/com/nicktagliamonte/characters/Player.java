package com.nicktagliamonte.characters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.nicktagliamonte.Spells.Spell;
import com.nicktagliamonte.game.GameState;
import com.nicktagliamonte.items.Amulet;
import com.nicktagliamonte.items.Armor;
import com.nicktagliamonte.items.Belt;
import com.nicktagliamonte.items.Boots;
import com.nicktagliamonte.items.Gloves;
import com.nicktagliamonte.items.HeadBand;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.items.Ring;
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
    @Expose private double intelligence;  
    @Expose private double wisdom;
    @Expose private double charisma;
    @Expose private double maxCarryWeight;
    @Expose private double remainingCarryWeight;
    @Expose private double ac;
    @Expose private double maxHealth;
    @Expose public Armor armor;
    @Expose public Gloves gloves;
    @Expose public Boots boots;
    @Expose public Belt belt;
    @Expose public HeadBand headBand;
    @Expose public Ring ring;
    @Expose public Amulet amulet;
    @Expose public Weapon weapon;
    @Expose public boolean hasWeapon;
    @Expose private List<Spell> spellbook;
    @Expose private int deathSavingThrows;
    @Expose private String status;
    @Expose public int timeSinceFood = 0;
    @Expose public int timeSinceWater = 0;
    @Expose private double alignment;
    @Expose private boolean isHiding;
    @Expose public int level;
    @Expose public double currentXP;
    @Expose public double nextLevelXP;
    @Expose public double nextLevelXPReward;
    @Expose private List<Quest> questLog;

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
        super.setHealth(characterClass.getHealth());
        this.ac = 10 + dexterity;
        this.maxHealth = characterClass.getHealth();
        this.spellbook = new ArrayList<>();
        this.deathSavingThrows = 0;
        this.status = " ";
        this.alignment = 0;
        this.isHiding = false;
        this.level = 1; 
        this.currentXP = 0;
        this.nextLevelXP = 100;
        this.nextLevelXPReward = 1;
        this.questLog = new ArrayList<>();
    }

    public List<Quest> getQuestLog() {
        return questLog;
    }

    public void setQuestLog(List<Quest> questLog) {
        this.questLog = questLog;
    }

    public void setSpellbook(List<Spell> spellbook) {
        this.spellbook = spellbook;
    }

    public int getTimeSinceFood() {
        return timeSinceFood;
    }

    public void setTimeSinceFood(int timeSinceFood) {
        this.timeSinceFood = timeSinceFood;
    }

    public int getTimeSinceWater() {
        return timeSinceWater;
    }

    public void setTimeSinceWater(int timeSinceWater) {
        this.timeSinceWater = timeSinceWater;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getCurrentXP() {
        return currentXP;
    }

    public void setCurrentXP(double currentXP) {
        this.currentXP = currentXP;
    }

    public double getNextLevelXP() {
        return nextLevelXP;
    }

    public void setNextLevelXP(double nextLevelXP) {
        this.nextLevelXP = nextLevelXP;
    }

    public double getNextLevelXPReward() {
        return nextLevelXPReward;
    }

    public void setNextLevelXPReward(double nextLevelXPReward) {
        this.nextLevelXPReward = nextLevelXPReward;
    }

    public Gloves getGloves() {
        return gloves;
    }

    public void setGloves(Gloves gloves) {
        this.gloves = gloves;
    }

    public void removeGloves() {
        gloves = null;
    }

    public Boots getBoots() {
        return boots;
    }

    public void setBoots(Boots boots) {
        this.boots = boots;
    }

    public void removeBoots() {
        boots = null;
    }

    public Belt getBelt() {
        return belt;
    }

    public void setBelt(Belt belt) {
        this.belt = belt;
    }

    public void removeBelt() {
        belt = null;
    }

    public HeadBand getHeadBand() {
        return headBand;
    }

    public void setHeadBand(HeadBand headBand) {
        this.headBand = headBand;
    }

    public void removeHeadBand() {
        headBand = null;
    }

    public Ring getRing() {
        return ring;
    }

    public void setRing(Ring ring) {
        this.ring = ring;
    }

    public void removeRing() {
        ring = null;
    }

    public Amulet getAmulet() {
        return amulet;
    }

    public void setAmulet(Amulet amulet) {
        this.amulet = amulet;
    }

    public void removeAmulet() {
        amulet = null;
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

    public void setHasWeapon() {

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
        return intelligence;
    }

    public void setIntelligence(double intelligence) {
        this.intelligence = intelligence;
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

    public void setRemainingCarryWeight(double input) {
        this.remainingCarryWeight = input;
    }

    public void listInventory() {
        List<Item> inventory = super.getInventory();
        
        if (inventory.isEmpty()) {
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
        
        if (inventory == null || inventory.isEmpty()) {
            System.out.println("No items in inventory");
            return null;
        }
        
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                if (item.getIsConsumable()) {
                    super.inventory.remove(item);
                    increaseRemainingCarryWeight(item.getWeight());
                }
                return item;
            }
        }
        
        return null;
    }    

    public void craftItem(Trap trap) {
        Map<Item, Integer> cost = trap.getCost();
        
        // Iterate over the cost to check if crafting is possible
        for (Map.Entry<Item, Integer> entry : cost.entrySet()) {
            Item item = entry.getKey();
            int requiredCount = entry.getValue();
    
            // Count the available items in the inventory
            long countInInventory = inventory.stream()
                    .filter(i -> i.getName().equals(item.getName()))
                    .count();
    
            // Check if there are enough items for crafting
            if (countInInventory >= requiredCount) {
                int removedCount = 0;
                Iterator<Item> iterator = inventory.iterator();
    
                // Remove the required items from the inventory
                while (iterator.hasNext() && removedCount < requiredCount) {
                    if (iterator.next().getName().equals(item.getName())) {
                        iterator.remove();
                        removedCount++;
                    }
                }
                
                // Add the crafted trap to inventory
                inventory.add(trap);
                System.out.println("Successfully crafted " + trap.getName());
            } else {
                // Inform the player if there are insufficient items
                System.out.println("Crafting failed: insufficient items in inventory.");
            }
        }
    }    

    public void increaseAC(int acBonus) {
        this.ac += acBonus;
    }

    public void setAc(double AC) {
        this.ac = AC;
    }

    public boolean hasItem(String itemName) {
        return inventory.stream()
                .anyMatch(item -> item.getName().equalsIgnoreCase(itemName));
    }    

    @SuppressWarnings("unused")
    public void hasKey(List<Adjacency> adjacencies) {
        adjacencies.forEach(adj -> inventory.stream()
                .filter(item -> item.getName().equalsIgnoreCase(adj.getAdjoiningRoomName()))
                .findFirst()
                .ifPresent(item -> adj.setIsLocked(false)));
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
        int baseDamage = (int) (Math.random() * strength) + 1;
        return hasWeapon ? weapon.getDamage() + baseDamage : baseDamage;
    }

    public boolean isDown() {
        return ((super.getHealth() > -maxHealth) && (super.getHealth() < 0));
    }

    public void takeDamage(double amount) {
        super.setHealth(super.getHealth() - amount);
    
        // General message
        String message;
    
        if (!isAlive()) {
            message = "You are fully dead.";
        } else if (isDown()) {
            message = "You are down and making death saving throws.";
            makeDeathSavingThrow();
        } else {
            message = "You took " + amount + " points of damage. Remaining health: " + getHealth();
        }
    
        System.out.println(message);
    }    

    public void makeDeathSavingThrow() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("Press enter to make a death saving throw.  11 or higher is a success, 10 or lower is a failure.");
        scanner.nextLine();
    
        int randomInt = (int) (Math.random() * 20) + 1;
        System.out.println("You rolled a " + randomInt + ".");
    
        // Update death saving throws based on roll
        if (randomInt <= 10) {
            deathSavingThrows -= 1;
            System.out.println("Failure! You are at " + deathSavingThrows + " on your death saving throws.");
        } else {
            deathSavingThrows += 1;
            System.out.println("Success! You are at " + deathSavingThrows + " on your death saving throws.");
        }
    
        // Check for stabilization or death
        if (deathSavingThrows == 3) {
            super.setHealth(0);
            deathSavingThrows = 0;
            System.out.println("You are stabilized at 0 health.");
        } else if (deathSavingThrows == -3) {
            super.setHealth(this.maxHealth * -1);
            deathSavingThrows = 0;
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
        intelligence += intelligence * 0.1;
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
        intelligence += intelligence * 0.0667;
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
        intelligence += intelligence * 0.0625;
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
            boolean isActive = false;
            
            // Check if any objective is not completed
            for (Objective objective : quest.getObjectives().values()) {
                if (!objective.getIsCompleted()) {
                    isActive = true;
                    break;
                }
            }
    
            // If at least one objective is not completed, the quest is active
            if (isActive) {
                activeQuests.add(quest);
            }
        }
    
        return activeQuests;
    }    

    public List<Quest> getCompletedQuests() {
        List<Quest> completedQuests = new ArrayList<>();
        
        for (Quest quest : questLog) {
            boolean isCompleted = true;
            
            // Check if any objective is not completed
            for (Objective objective : quest.getObjectives().values()) {
                if (!objective.getIsCompleted()) {
                    isCompleted = false;
                    break;
                }
            }
    
            // If all objectives are completed, the quest is completed
            if (isCompleted) {
                completedQuests.add(quest);
            }
        }
    
        return completedQuests;
    }    

    public void addItemToInventory(Item item) {
        inventory.add(item);
        reduceRemainingCarryWeight(item.getWeight());
    }
    
    public void removeItemFromInventory(Item item) {
        // Use iterator to avoid ConcurrentModificationException when removing items while iterating. which took longer to figure out than it should have.
        Iterator<Item> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            Item itemInInventory = iterator.next();
            if (item.getName().equalsIgnoreCase(itemInInventory.getName())) {
                iterator.remove();  // Safely remove item while iterating
                reduceRemainingCarryWeight(itemInInventory.getWeight());
                break;  // Exit after removing the first matching item
            }
        }
    }   
    
    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
        // Convert the Player object to a base JSON string
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
    
        // Serialize equipment with toSerializableFormat
        jsonObject.addProperty("armor", armor != null ? armor.toSerializableFormat() : null);
        jsonObject.addProperty("gloves", gloves != null ? gloves.toSerializableFormat() : null);
        jsonObject.addProperty("boots", boots != null ? boots.toSerializableFormat() : null);
        jsonObject.addProperty("belt", belt != null ? belt.toSerializableFormat() : null);
        jsonObject.addProperty("headBand", headBand != null ? headBand.toSerializableFormat() : null);
        jsonObject.addProperty("ring", ring != null ? ring.toSerializableFormat() : null);
        jsonObject.addProperty("amulet", amulet != null ? amulet.toSerializableFormat() : null);
        jsonObject.addProperty("weapon", weapon != null ? weapon.toSerializableFormat() : null);
    
        // Serialize spellbook, with a null check for the list
        JsonArray spellbookArray = new JsonArray();
        if (spellbook != null) {
            for (Spell spell : spellbook) {
                spellbookArray.add(JsonParser.parseString(spell.toSerializableFormat()));
            }
        }
        jsonObject.add("spellbook", spellbookArray);
    
        // Serialize questLog, with a null check for the list
        JsonArray questLogArray = new JsonArray();
        if (questLog != null) {
            for (Quest quest : questLog) {
                questLogArray.add(JsonParser.parseString(quest.toSerializableFormat()));
            }
        }
        jsonObject.add("questLog", questLogArray);
    
        // Add the final JSON string
        return gson.toJson(jsonObject);
    }    
}