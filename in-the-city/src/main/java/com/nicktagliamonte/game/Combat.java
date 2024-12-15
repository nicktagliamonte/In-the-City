package com.nicktagliamonte.game;

import java.util.ArrayList;
import java.util.Comparator;
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
    public List<Quest> questLog = new ArrayList<>();
    public List<Quest> activeQuests = new ArrayList<>();

    public Combat(List<Person> combatants, GameState gameState, String location) {
        this.combatants = combatants;
        this.gameState = gameState;
        this.location = location;
        this.questLog = gameState.getPlayer().getAllQuests();
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
        combatLoop(combatants);
    }

    private void combatLoop(List<Person> combatants) {
        boolean combatActive = true;

        while (combatActive) {
            Iterator<Person> combatantIterator = combatants.iterator(); // Use iterator
            while (combatantIterator.hasNext()) {
                Person combatant = combatantIterator.next();

                // this only needs 2 because it's a player vs everyone else check
                if (combatant instanceof Player) {
                    Player player = (Player) combatant;
                    if (player.getHealth() <= -(player.getMaxHealth())) {
                        combatActive = false;
                        gameState.playerDead();
                    }
                } else {
                    NPC npcCombatant = (NPC) combatant;
                    if (npcCombatant.isDead()) {
                        //remove the combatant from the list of combatants
                        combatantIterator.remove();

                        //remove the npc from the current room
                        Map<String, NPC> people = gameState.getCurrentRoom().getPeopleInRoom();
                        Iterator<Map.Entry<String, NPC>> iterator = people.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, NPC> entry = iterator.next();
                            String location = entry.getKey();
                            System.out.println(location);
                            if (entry.getValue().getName().equals(npcCombatant.getName())) {
                                iterator.remove();
                                gameState.getCurrentRoom().updateMapEntry('.', Character.getNumericValue(location.charAt(1)), Character.getNumericValue(location.charAt(3)));
                                break;
                            }
                        }
                        gameState.getCurrentRoom().setPeopleInRoom(people);

                        if (npcCombatant instanceof PartyMember) {
                            System.out.println("A member of your party, " + npcCombatant.getName()
                                    + ", has fully died. They cannot be revived or re-encountered.\n" +
                                    "If you continue from this point, they will be gone from this world forever.\n" +
                                    "It may be worth considering reloading your last save and taking a different approach to this battle, or avoiding it altogether.");
                        }
                        continue;
                    }
                }

                // this DOES need the 3, because player/partymember/adversary turns are all
                // handled differently
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
        }

        List<Adversary> adversaries = new ArrayList<>();
        for (Person combatant : combatants) {
            if (combatant instanceof Adversary) {
                adversaries.add((Adversary) combatant);
            }
        }

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
                    + " has " + target.getHealth() + " health left");
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
            System.out.println("Your inventory is empty! Turn wasted.");
            return;
        }

        System.out.println("Choose an item to use (0 to cancel):");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i).getName() + " - " + inventory.get(i).getDescription());
        }

        int choice = gameState.getGameEngine().getPlayerInputAsInt();
        if (choice == 0) {
            System.out.println("You chose not to use any item.");
            return;
        }

        Item selectedItem = inventory.get(choice - 1);
        System.out.println("You used: " + selectedItem.getName());
        selectedItem.use(gameState); // this should work based on runtime polymorphism (the use method from the item
                            // subclass will be called)
        if (selectedItem.getIsConsumable()) {
            inventory.remove(selectedItem);
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
                        + target.getName() + " has " + target.getHealth() + " health left");
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
                    + player.getHealth() + " health left");
        } else {
            System.out.println(attacker.getName() + " attacks you, but misses");
        }
    }

    private boolean checkVictory(List<Person> combatants) {
        return combatants.stream()
                .noneMatch(c -> c instanceof Adversary && ((NPC) c).isAlive());
    }

    private boolean checkDefeat(List<Person> combatants) {
        for (Person c : combatants) {
            if (c instanceof Player) {
                if (((Player) c).isAlive()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void removeDeadCombatants() {
        Map<String, NPC> people = gameState.getCurrentRoom().getPeopleInRoom();
        Iterator<Map.Entry<String, NPC>> iterator = people.entrySet().iterator();

        for (Person person : combatants) {
            if (person instanceof Adversary) {
                for (Quest quest : activeQuests) {
                    for (Objective objective : quest.getObjectives().values()) {
                        if ((!objective.getIsCompleted()) && objective.getType().equalsIgnoreCase("combat")){
                            if (person.getName().equalsIgnoreCase(objective.getTarget())) {
                                quest.completeObjective(objective.getId());
                                break;
                            }
                            break;
                        }
                    }
                }

                while (iterator.hasNext()) {
                    Map.Entry<String, NPC> entry = iterator.next();
                    String location = entry.getKey();
                    if (entry.getValue().getName().equals(person.getName())) {
                        List<Item> inventory = entry.getValue().getInventory();
                        for (Item item : inventory) {
                            gameState.getCurrentRoom().addItemToRoom(location, item);
                        }
                        gameState.getCurrentRoom().updateMapEntry('I', Character.getNumericValue(location.charAt(1)), Character.getNumericValue(location.charAt(3)));
                        iterator.remove();
                    }
                }
            }
        }

        gameState.getCurrentRoom().setPeopleInRoom(people);
    }
}