package com.nicktagliamonte.characters;
public class CharacterClass {
    private double strength;
    private double dexterity;
    private double constitution;
    private double intelligence;
    private double wisdom;
    private double charisma;
    private double maxCarryWeight;
    private double health;
    private double energy;

    public CharacterClass(String className) {
        if (className.equalsIgnoreCase("survivalist")) {
            this.strength = 10;
            this.dexterity = 8;
            this.constitution = 10;
            this.intelligence = 6;
            this.wisdom = 6;
            this.charisma = 8;
            this.maxCarryWeight = 15;
            this.energy = 5;
            this.health = 22;
        } else if (className.equalsIgnoreCase("technologist")) {
            this.strength = 6;
            this.dexterity = 8;
            this.constitution = 8;
            this.intelligence = 12;
            this.wisdom = 10;
            this.charisma = 6;
            this.maxCarryWeight = 13;
            this.energy = 5;
            this.health = 18;
        } else if (className.equalsIgnoreCase("negotiator")) {
            this.strength = 8;
            this.dexterity = 8;
            this.constitution = 10;
            this.intelligence = 8;
            this.wisdom = 12;
            this.charisma = 12;
            this.maxCarryWeight = 14;
            this.energy = 5;
            this.health = 20;
        }
    }

    public static CharacterClass createCharacterClass(String input) {
        if ("survivalist".equalsIgnoreCase(input) || "technologist".equalsIgnoreCase(input) || "negotiator".equalsIgnoreCase(input)) {
            return new CharacterClass(input);
        } else {
            System.out.println("Invalid character class. Please try again.");
            return null;
        }
    }

    public double getStrength() {
        return strength;
    }

    public double getDexterity() {
        return dexterity;
    }

    public double getConstitution() {
        return constitution;
    }

    public double getIntelligence() {
        return intelligence;
    }

    public double getWisdom() {
        return wisdom;
    }

    public double getCharisma() {
        return charisma;
    }

    public double getMaxCarryWeight() {
        return maxCarryWeight;
    }

    public double getHealth() {
        return health;
    }

    public double getEnergy() {
        return energy;
    }

    
}
