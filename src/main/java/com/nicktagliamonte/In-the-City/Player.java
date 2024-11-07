public class Player extends Person {
    private CharacterClass characterClass;
    private double strength;
    private double dexterity;
    private double constitution;
    private double doubleelligence;
    private double wisdom;
    private double charisma;
    private double maxCarryWeight;

    public Player() {
        super();
    }

    public Player(String name, int health, int energy, CharacterClass characterClass, double strength, double dexterity,
            double constitution, double doubleelligence, double wisdom, double charisma, double maxCarryWeight) {
        super(name, health, energy);
        this.characterClass = characterClass;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.doubleelligence = doubleelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.maxCarryWeight = maxCarryWeight;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
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

    public double getDoubleelligence() {
        return doubleelligence;
    }

    public void setDoubleelligence(double doubleelligence) {
        this.doubleelligence = doubleelligence;
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
}