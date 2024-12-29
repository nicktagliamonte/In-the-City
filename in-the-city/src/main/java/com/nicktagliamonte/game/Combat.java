package com.nicktagliamonte.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nicktagliamonte.Spells.Spell;
import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.PartyMember;
import com.nicktagliamonte.characters.Person;
import com.nicktagliamonte.characters.Player;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.quests.Objective;
import com.nicktagliamonte.quests.Quest;

public class Combat {
    public List<Person> combatants = new ArrayList<>();
    public GameState gameState;
    public String location;
    public List<Quest> activeQuests = new ArrayList<>();

    public Combat(List<Person> combatants, GameState gameState, String location) {
        this.combatants = combatants;
        this.gameState = gameState;
        this.location = location;
        this.activeQuests = gameState.getPlayer().getActiveQuests();
        combatLoop(combatants);
    }

    private void combatLoop(List<Person> combatants) {
        boolean combatActive = true;
    
        while (combatActive) {
            Iterator<Person> combatantIterator = combatants.iterator(); // Use iterator
            while (combatantIterator.hasNext()) {
                Person combatant = combatantIterator.next();
    
                // Check for player death
                if (combatant instanceof Player) {
                    Player player = (Player) combatant;
                    if (player.getHealth() <= -(player.getMaxHealth())) {
                        combatActive = false;
                        gameState.playerDead();
                        break; // Exit combat loop immediately
                    }
                } else {
                    NPC npcCombatant = (NPC) combatant;
                    if (npcCombatant.isDead()) {
                        // Remove the combatant from the list of combatants
                        combatantIterator.remove();
                        
                        // Remove the NPC from the current room and update the map
                        Map<String, NPC> people = gameState.getCurrentRoom().getPeopleInRoom();
                        people.entrySet().removeIf(entry -> {
                            String location = entry.getKey();
                            if (entry.getValue().getName().equals(npcCombatant.getName())) {
                                gameState.getCurrentRoom().updateMapEntry('.', Character.getNumericValue(location.charAt(1)), Character.getNumericValue(location.charAt(3)));
                                return true;
                            }
                            return false;
                        });
                        gameState.getCurrentRoom().setPeopleInRoom(people);
    
                        // Handle party member death
                        if (npcCombatant instanceof PartyMember) {
                            System.out.println("A member of your party, " + npcCombatant.getName()
                                    + ", has fully died. They cannot be revived or re-encountered.\n" +
                                    "If you continue from this point, they will be gone from this world forever.\n" +
                                    "It may be worth considering reloading your last save and taking a different approach to this battle, or avoiding it altogether.");
                        }
                        continue; // Skip to next combatant
                    }
                }
    
                // Handle combatant turns
                if (combatant instanceof Player) {
                    if (!playerTurn((Player) combatant)) {
                        combatActive = false; // If the player flees, stop the combat loop
                        break;
                    }
                } else if (combatant instanceof PartyMember) {
                    partyMemberTurn((PartyMember) combatant);
                } else if (combatant instanceof Adversary) {
                    adversaryTurn((Adversary) combatant);
                }
    
                // Check for victory/defeat
                if (checkVictory(combatants)) {
                    System.out.println("You win!");
                    removeDeadCombatants();
                    combatActive = false;
                    gameState.getPlayer().gainXP(40, gameState);
                    break;
                } else if (checkDefeat(combatants)) {
                    System.out.println("You lose!");
                    combatActive = false;
                    gameState.getPlayer().gainXP(10, gameState);
                    break;
                }
            }
        }
    }    

    private boolean playerTurn(Player player) {
        if (player.isDown()) {
            System.out.println("You are down and need to make death saving throws");
            player.makeDeathSavingThrow();
            return true;
        }
    
        if (!player.isAlive()) {
            gameState.playerDead();
            return false; // End the turn if the player is dead
        }
    
        // Collect adversaries
        List<Adversary> adversaries = new ArrayList<>();
        for (Person combatant : combatants) {
            if (combatant instanceof Adversary) {
                adversaries.add((Adversary) combatant);
            }
        }
    
        // Display turn options
        System.out.println("Your turn! Choose an action:");
        System.out.println("1. Attack");
        System.out.println("2. Use Item");
        System.out.println("3. Cast Spell");
        System.out.println("4. Flee");
    
        int choice = gameState.getGameEngine().getPlayerInputAsInt(); // Assume this handles user input
    
        switch (choice) {
            case 1 -> attack(player);
            case 2 -> useItem(player);
            case 3 -> castSpell(player);
            case 4 -> {
                if (attemptToflee(player, adversaries)) {
                    return false; // If fleeing is successful, return false to end combat
                }
            }
            default -> System.out.println("Invalid choice, turn skipped!");
        }
        return true; // Continue combat if no flee attempt or flee failed
    }    

    private void attack(Player player) {
        // Get the target adversary with the lowest health
        Adversary target = getLowestHealthAdversary();
        if (target == null) {
            System.out.println("No valid target to attack!");
            return;
        }
    
        // Roll to hit
        int attackRoll = gameState.rollD20() + player.getAttackModifier();
        if (player.inHunger()) {
            attackRoll -= 2;
        }
        System.out.println("You roll to hit: " + attackRoll + " (vs AC " + target.getAc() + ")");
    
        if (attackRoll >= target.getAc()) {
            // Roll for damage
            int damage = player.rollWeaponDamage();
            if (player.inHunger()) {
                damage -= player.getLevel();
                if (damage < 1) {
                    damage = 1;
                }
            }
            target.takeDamage(damage);
            System.out.println("You hit " + target.getName() + " for " + damage + " damage! " + target.getName()
                    + " has " + String.format("%.2f", target.getHealth()) + " health left");
        } else {
            System.out.println("Your attack misses!");
        }
    }    

    private Adversary getLowestHealthAdversary() {
        return combatants.stream()
                .filter(c -> c instanceof Adversary && ((NPC) c).isAlive())
                .map(c -> (Adversary) c)
                .min(Comparator.comparingDouble(NPC::getHealth))
                .orElse(null);
    }

    public void useItem(Player player) {
        List<Item> inventory = player.getInventory();

        if (inventory.isEmpty()) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Your inventory is empty! Turn wasted.");
            return;
        }

        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Display items and their quantities in inventory
        System.out.println("Choose an item to use (0 to cancel):");
        System.out.println("────────────────────────────────────────────────");

        // Create a map to track item quantities
        Map<String, Integer> itemQuantities = new HashMap<>();
        
        // Count the quantities of each item
        for (Item item : inventory) {
            itemQuantities.put(item.getName(), itemQuantities.getOrDefault(item.getName(), 0) + 1);
        }

        // Display items with quantities
        int index = 1;
        for (Item item : inventory) {
            int quantity = itemQuantities.get(item.getName());  // Get the quantity from the map
            System.out.println(index++ + ". " + item.getName() + " - " + item.getDescription() + " (x" + quantity + ")");
        }

        int choice = gameState.getGameEngine().getPlayerInputAsInt();
        if (choice == 0) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You chose not to use any item.");
            return;
        }

        Item selectedItem = inventory.get(choice - 1);
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("You used: " + selectedItem.getName());
        selectedItem.use(gameState); // This should work based on runtime polymorphism (the use method from the item subclass will be called)

        if (selectedItem.getIsConsumable()) {
            // Decrease the quantity in the player's inventory
            int quantity = itemQuantities.get(selectedItem.getName());
            if (quantity > 1) {
                // Update the inventory to reflect the reduced quantity
                itemQuantities.put(selectedItem.getName(), quantity - 1);
            } else {
                inventory.remove(selectedItem);
            }
        }
    }

    private void castSpell(Player player) {
        Adversary target = getLowestHealthAdversary();
        if (target == null) {
            System.out.println("No valid target to attack!");
            return;
        }
    
        List<Spell> spells = player.getSpellbook();
    
        if (spells.isEmpty()) {
            System.out.println("You don't know any spells!");
            return;
        }
    
        System.out.println("Choose a spell to cast:");
        for (int i = 0; i < spells.size(); i++) {
            Spell spell = spells.get(i);
            System.out.println((i + 1) + ". " + spell.getName() + " - " + spell.getDescription());
        }
        System.out.println("0. Cancel");
    
        int choice = gameState.getGameEngine().getPlayerInputAsInt();
    
        if (choice == 0) {
            System.out.println("You canceled casting a spell.");
            return;
        }
    
        if (choice < 1 || choice > spells.size()) {
            System.out.println("Invalid choice!");
            return;
        }
    
        Spell selectedSpell = spells.get(choice - 1);
        selectedSpell.cast(player, target);
    }    

    private boolean attemptToflee(Player player, List<Adversary> adversaries) {
        int dex = (int) Math.floor(player.getDexterity());
        if (player.inHunger()) {
            dex -= 1;
        }
    
        // Calculate a DC (Difficulty Class) for the escape attempt
        int escapeDC = calculateEscapeDC(adversaries);
    
        // Roll a d20 and add the player's relevant attribute modifier
        int roll = gameState.rollD20();
        int total = roll + getModifier(dex);
    
        System.out.println("You rolled: " + roll + " + Dexterity Modifier (" + getModifier(dex) + ") = " + total);
        System.out.println("Escape DC: " + escapeDC);
    
        if (total >= escapeDC) {
            System.out.println("You successfully escaped combat!");
            return true;
        } else {
            System.out.println("You failed to escape combat!");
            return false; // Escape fails
        }
    }    

    private int calculateEscapeDC(List<Adversary> adversaries) {
        if (adversaries.isEmpty()) {
            return 0; // Default DC if no adversaries
        }

        return adversaries.stream()
                .map(a -> (Adversary) a)
                .mapToInt(adversary -> {
                    int dexterityMod = getModifier((int) Math.floor(adversary.getDexterity()));
                    int strengthMod = getModifier((int) Math.floor(adversary.getStrength()));
                    int healthFactor = (int) (adversary.getHealth() / 10.0);

                    return 10 + dexterityMod + strengthMod + healthFactor;
                })
                .max()
                .orElse(0);
    }

    private int getModifier(int attribute) {
        return (attribute - 10) / 2;
    }

    private void partyMemberTurn(PartyMember partyMember) {
        if (partyMember.isDown()) {
            System.out.println(partyMember.getName() + " is down and needs to make death saving throws");
            partyMember.makeDeathSavingThrow();
            return;
        }
    
        Adversary target = getLowestHealthAdversary();
        if (target != null) {
            attack(partyMember, target);
        }
    }    

    private void adversaryTurn(NPC adversary) {
        Person target = getLowestHealthPlayerOrParty();
        if (target != null) {
            attack(adversary, target);
        }
    }

    private Person getLowestHealthPlayerOrParty() {
        PartyMember lowestPartyMember = combatants.stream()
                .filter(c -> c instanceof PartyMember && ((NPC) c).isAlive() && c.getHealth() >= 0)
                .map(c -> (PartyMember) c)
                .min(Comparator.comparingDouble(NPC::getHealth))
                .orElse(null);
        if (lowestPartyMember == null) {
            return gameState.getPlayer();
        } else {
            return (lowestPartyMember.getHealth() < gameState.getPlayer().getHealth()) ? lowestPartyMember
                    : gameState.getPlayer();
        }
    }

    private void attack(NPC attacker, Person target) {
        if (target instanceof Player) {
            attackPlayer((Adversary) attacker, (Player) target);
        } else {
            NPC npcTarget = (NPC) target;
            int attackRoll = gameState.rollD20() + attacker.getAttackModifier();
    
            if (attackRoll >= npcTarget.getAc()) {
                int damage = 0;
    
                if (attacker instanceof PartyMember) {
                    PartyMember partyMemberAttacker = (PartyMember) attacker;
                    Adversary adversaryTarget = (Adversary) target;
                    damage = (int) Math.floor(partyMemberAttacker.getDamage());
                    adversaryTarget.takeDamage(damage);
                } else {
                    Adversary adversaryAttacker = (Adversary) attacker;
                    PartyMember partyMemberTarget = (PartyMember) target;
                    damage = (int) Math.floor(adversaryAttacker.getDamage());
                    partyMemberTarget.takeDamage(damage);
                }
    
                System.out.println(attacker.getName() + " hits " + target.getName() + " for " + damage + " damage! "
                    + target.getName() + " has " + String.format("%.2f", target.getHealth()) + " health left");
    
                if (!npcTarget.isAlive() && target instanceof Adversary) {
                    System.out.println(target.getName() + " is defeated!");
                }
            } else {
                System.out.println(attacker.getName() + " misses " + target.getName());
            }
        }
    }    

    private void attackPlayer(Adversary attacker, Player player) {
        if (player.isDown()) {
            System.out.println(attacker.getName()
                    + " cannot currently attack you, as you are down and making death saving throws");
            return;
        }
    
        int attackRoll = (int) Math.floor(gameState.rollD20() + attacker.getStrength());
        double ac = player.getAc();
    
        if (player.inHunger()) {
            ac -= 2;
        }
    
        if (attackRoll >= ac) {
            // Roll for damage
            int damage = (int) Math.floor(attacker.getDamage());
            player.takeDamage(damage);
            System.out.println(attacker.getName() + " hits you for " + damage + " damage. You have "
                    + String.format("%.2f", player.getHealth()) + " health left");
        } else {
            System.out.println(attacker.getName() + " attacks you, but misses");
        }
    }    

    private boolean checkVictory(List<Person> combatants) {
        return combatants.stream()
                .noneMatch(c -> c instanceof Adversary && ((NPC) c).isAlive());
    }

    private boolean checkDefeat(List<Person> combatants) {
        return combatants.stream()
                .filter(c -> c instanceof Player)
                .allMatch(c -> !((Player) c).isAlive());
    }    

    private void removeDeadCombatants() {
        Map<String, NPC> people = gameState.getCurrentRoom().getPeopleInRoom();
    
        for (Person person : combatants) {
            if (person instanceof Adversary) {
                // Check and complete quest objectives related to combat
                completeCombatQuestObjectives((Adversary) person);
    
                // Remove the adversary from the room and transfer its inventory
                removeAdversaryFromRoom((Adversary) person, people);
            }
        }
    
        gameState.getCurrentRoom().setPeopleInRoom(people);
    }
    
    private void completeCombatQuestObjectives(Adversary adversary) {
        for (Quest quest : activeQuests) {
            for (Objective objective : quest.getObjectives().values()) {
                if (!objective.getIsCompleted() && "combat".equalsIgnoreCase(objective.getType()) 
                    && adversary.getName().equalsIgnoreCase(objective.getTarget())) {
                    quest.completeObjective(objective.getId());
                    break; // Break once the objective is completed
                }
            }
        }
    }
    
    private void removeAdversaryFromRoom(Adversary adversary, Map<String, NPC> people) {
        people.entrySet().removeIf(entry -> {
            if (entry.getValue().getName().equalsIgnoreCase(adversary.getName())) {
                // Transfer items from adversary to the room
                List<Item> inventory = entry.getValue().getInventory();
                String location = entry.getKey();
                for (Item item : inventory) {
                    gameState.getCurrentRoom().addItemToRoom(location, item);
                }
    
                // Update map
                gameState.getCurrentRoom().updateMapEntry('I', 
                    Character.getNumericValue(location.charAt(1)), 
                    Character.getNumericValue(location.charAt(3)));
                return true; // Remove the entry from the map
            }
            return false;
        });
    }    
}