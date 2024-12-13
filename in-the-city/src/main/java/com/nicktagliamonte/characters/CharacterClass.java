package com.nicktagliamonte.characters;

import com.google.gson.annotations.Expose;

public class CharacterClass {
    @Expose private String className;
    @Expose private double strength;
    @Expose private double dexterity;
    @Expose private double constitution;
    @Expose private double Intelligence;
    @Expose private double wisdom;
    @Expose private double charisma;
    @Expose private double maxCarryWeight;
    @Expose private double health;
    @Expose private double ac;

    //TODO: figure out ac
    public CharacterClass(String className) {
        if (className.equalsIgnoreCase("survivalist")) {
            this.className = "survivalist";
            this.strength = 10;
            this.dexterity = 8;
            this.constitution = 10;
            this.Intelligence = 6;
            this.wisdom = 6;
            this.charisma = 8;
            this.maxCarryWeight = 15;
            this.health = 22;
            this.ac = 5;
        } else if (className.equalsIgnoreCase("technologist")) {
            this.className = "technologist";
            this.strength = 6;
            this.dexterity = 8;
            this.constitution = 8;
            this.Intelligence = 12;
            this.wisdom = 10;
            this.charisma = 6;
            this.maxCarryWeight = 13;
            this.health = 18;
            this.ac = 5;
        } else if (className.equalsIgnoreCase("negotiator")) {
            this.className = "negotiator";
            this.strength = 8;
            this.dexterity = 8;
            this.constitution = 10;
            this.Intelligence = 8;
            this.wisdom = 12;
            this.charisma = 12;
            this.maxCarryWeight = 14;
            this.health = 20;
            this.ac = 5;
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

    public String getClassName() {
        return className;
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
        return Intelligence;
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

    public double getAc() {
        return ac;
    }
}
