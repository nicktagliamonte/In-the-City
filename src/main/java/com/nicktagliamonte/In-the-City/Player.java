import java.util.List;

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

    public void listInventory() {
        List<Item> inventory = super.getInventory();
        if (inventory == null) {
            System.out.println("No items in inventory");
        } else {
            inventory.forEach(item -> System.out.println(item));
        }
    }

    public void hide() {
        //unsure of exact gameplay elements here, i think this will require feedback from gamestate
        //like the success chance depends on the adversaries in the region
        System.out.println("working");
    }

    public void useHint() {
        //gonna need to add a field and a setter for hints
        //in this method, there will be a check on the hints available 
        //then display hint and alter the counter
        System.out.println("working");
    }

    public Item getItemFromInventory(String itemName) {
        List<Item> inventory = super.getInventory();
        if (inventory == null) {
            System.out.println("No items in inventory");
        } else {
            for (Item item : inventory) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    return item;
                }
            }
        }
        return null;
    }
}