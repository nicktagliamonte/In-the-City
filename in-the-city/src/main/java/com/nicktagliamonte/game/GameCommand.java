package com.nicktagliamonte.game;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import com.nicktagliamonte.characters.Friend;
import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.PartyMember;
import com.nicktagliamonte.characters.Person;
import com.nicktagliamonte.items.Armor;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.items.Weapon;

public enum GameCommand {
    LOOK {
        @Override
        public void execute(String[] args, GameState gameState) {
            // Get the current position from GameState
            String currentRoom = gameState.getCurrentRoom().getName();
            String roomDescription = gameState.getRoomDescription(currentRoom);
            Map<String, Item> visibleItems = gameState.getCurrentRoom().getItemsInRoom();
            Map<String, NPC> characters = gameState.getCurrentRoom().getPeopleInRoom();
            List<NPC> party = gameState.getCurrentParty();

            // Construct the dynamic message based on the game state
            StringBuilder message = new StringBuilder();
            message.append("You look around and see: \n").append(roomDescription).append("\n");
            if (gameState.getCurrentRoom().getIsSafe()) {
                message.append("This is the region's safe zone. There will be no combat events here unless you initiate them.\n");
            }

            if (!visibleItems.isEmpty()) {
                message.append("Items here: ");
                // Iterate over the entries of the visibleItems map
                for (Map.Entry<String, Item> entry : visibleItems.entrySet()) {
                    // For each item, append its name and key (coordinates)
                    message.append(entry.getValue().getName())
                            .append(" at ")
                            .append(entry.getKey());

                    // Add a comma between items, but not after the last item
                    if (visibleItems.size() > 1
                            && !entry.equals(visibleItems.entrySet().toArray()[visibleItems.size() - 1])) {
                        message.append(", ");
                    }
                }
                message.append("\n");
            } else {
                message.append("There are no notable items here.\n");
            }

            if (!characters.isEmpty()) {
                message.append("People here: ");
                for (Map.Entry<String, NPC> entry : characters.entrySet()) {
                    message.append(entry.getValue().getName())
                            .append(" at ")
                            .append(entry.getKey());

                    if (characters.size() > 1
                            && !entry.equals(characters.entrySet().toArray()[characters.size() - 1])) {
                        message.append(", ");
                    }
                }
                message.append("\n");
            } else {
                message.append("There are no notable people here.\n");
            }

            if (!party.isEmpty()) {
                message.append("Party members here: ");
                for (NPC partyMember : party) {
                    message.append(partyMember.getName());

                    if (party.size() > 1 && !partyMember.equals(party.toArray()[party.size() - 1])) {
                        message.append(", ");
                    }
                }
            } else {
                message.append("There are no party members with you.");
            }

            // Print the message
            System.out.println(message.toString());
            gameState.getCurrentRoom().viewAdjascentRooms();
        }
    },
    MOVE {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                System.out.println("Which direction do you want to go?");
            } else {
                String[] arguments = args[0].split(" ");
                if (arguments.length == 0) {
                    System.out.println("Invalid command format.");
                    return;
                }

                if (arguments[0].equalsIgnoreCase("to") && arguments.length > 1) {
                    StringBuilder waypointNameBuilder = new StringBuilder();
                    for (int i = 1; i < arguments.length; i++) {
                        waypointNameBuilder.append(arguments[i]);
                        if (i < arguments.length - 1) {
                            waypointNameBuilder.append(" ");
                        }
                    }
                    String waypointName = waypointNameBuilder.toString();
                    String message = gameState.moveToWaypoint(waypointName);
                    System.out.println(message);
                    return;
                }

                // Handle regular movement
                String direction = arguments[0];
                int distance = 1; // Default distance

                if (arguments.length > 1) {
                    try {
                        distance = Integer.parseInt(arguments[1]);
                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Invalid number of steps. Please enter a valid integer, or just a direction to move 1 step in that direction.");
                        return;
                    }
                }
                String message = gameState.changeLocation(direction, distance);
                if (!(message == null)) {
                    System.out.println(message);
                }
            }
        }
    },
    ENTER {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                System.out.println("Which room do you want to enter?");
                @SuppressWarnings("resource")
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                gameState.enterByCommand(input);
            } else {
                gameState.enterByCommand(args[0]);
            }
        }
    },
    ASCEND {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 2) {
                System.out.println("Which room do you want to ascend to?");
                @SuppressWarnings("resource")
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                gameState.ascend(input);
            } else {
                gameState.ascend(args[1]);
            }
        }
    },
    DESCEND {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 2) {
                System.out.println("Which room do you want to descend to?");
                @SuppressWarnings("resource")
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                gameState.descend(input);
            } else {
                gameState.descend(args[1]);
            }
        }
    },
    EXAMINE {
        @Override
        public void execute(String[] args, GameState gameState) {
            String itemName = "";

            if (args.length < 1 && gameState.itemContext.equals("")) {
                System.out.println("You need to use this command in the form Examine [Item Name]");
                return;
            } else if (args.length < 1) {
                itemName = gameState.itemContext;
            } else {
                itemName = args[0];
            }

            Collection<Item> itemsInRoom = gameState.getCurrentRoom().getItemsInRoom().values();
            if (gameState.getCurrentRoom().getIsSafe()) {
                itemsInRoom.addAll(gameState.safeZoneInventory.getInventory());
            }
            Item itemToExamine = null;
            for (Item item : itemsInRoom) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    itemToExamine = item;
                    break;
                }
            }

            if (itemToExamine != null) {
                System.out.println(itemToExamine.getDescription());
            } else {
                System.out.println("There is no such item here: " + args[0]);
            }

            itemsInRoom.clear();
            gameState.itemContext = "";
        }
    },
    TAKE {
        @Override
        public void execute(String[] args, GameState gameState) {
            String itemName = "";
    
            if (args.length < 1 && gameState.itemContext.equals("")) {
                System.out.println("You need to use this command in the form TAKE [Item Name]");
                return;
            } else if (args.length < 1) {
                itemName = gameState.itemContext;
            } else {
                itemName = args[0];
            }
    
            Item itemToTake = null;
    
            if (gameState.getCurrentRoom().getIsSafe()) {
                itemToTake = gameState.safeZoneInventory.getItemFromInventory(itemName);
    
                if (itemToTake != null) {
                    if (gameState.getPlayer().getRemainingCarryWeight() > itemToTake.getWeight()) {
                        gameState.getPlayer().addItem(itemToTake);
                        gameState.getPlayer().reduceRemainingCarryWeight(itemToTake.getWeight());
                        System.out.printf("added %s to inventory from the safe zone inventory\n", itemToTake.getName());
                        gameState.itemContext = itemToTake.getName();
                    } else {
                        System.out.println("That item is too heavy.");
                    }
                    return;
                } else {
                    System.out.println("No such item found in the safe zone inventory: " + itemName);
                }
            }
    
            Map<String, Item> itemsInRoom = gameState.getCurrentRoom().getItemsInRoom();
            String itemLocation = null;
            for (Entry<String, Item> item : itemsInRoom.entrySet()) {
                if (item.getValue().getName().equalsIgnoreCase(itemName)) {
                    itemToTake = item.getValue();
                    itemLocation = item.getKey();
                    gameState.getCurrentRoom().updateMapEntry('.', 
                            Character.getNumericValue(itemLocation.charAt(1)), 
                            Character.getNumericValue(itemLocation.charAt(3)));
                    break;
                }
            }
    
            if (itemToTake != null) {
                if (gameState.getPlayer().getRemainingCarryWeight() > itemToTake.getWeight()) {
                    gameState.getPlayer().addItem(itemToTake);
                    gameState.getPlayer().reduceRemainingCarryWeight(itemToTake.getWeight());
                    gameState.getCurrentRoom().removeItemFromRoom(itemLocation);
                    System.out.printf("added %s to inventory\n", itemToTake.getName());
                    gameState.itemContext = itemToTake.getName();
                } else {
                    System.out.println("That item is too heavy.");
                }
            } else {
                System.out.println("There is no such item here: " + itemName);
            }
        }
    },
    DROP {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                System.out.println("You need to specify an item to drop");
                return;
            }

            Item itemToDrop = null;
            List<Item> itemsInInventory = gameState.getPlayer().getInventory();

            if (itemsInInventory == null && !gameState.getCurrentRoom().getIsSafe()) {
                System.out.println("Inventory is empty, cannot drop item");
                return;
            }

            for (Item item : itemsInInventory) {
                if (item.getName().equalsIgnoreCase(args[0])) {
                    itemToDrop = item;
                    gameState.getCurrentRoom().updateMapEntry('I',
                            Character.getNumericValue(gameState.getCurrentRoom().getPlayerPosition().charAt(1)),
                            Character.getNumericValue(gameState.getCurrentRoom().getPlayerPosition().charAt(4)));
                    break;
                }
            }

            if (itemToDrop == null && !gameState.getCurrentRoom().getIsSafe()) {
                System.out.printf("There is no %s in inventory", args[0]);
            } else {
                itemsInInventory.remove(itemToDrop);
                gameState.getPlayer().setInventory(itemsInInventory);
                gameState.getPlayer().increaseRemainingCarryWeight(itemToDrop.getWeight());
                gameState.getCurrentRoom().addItemToRoom(gameState.getCurrentRoom().getPlayerPosition(),
                        itemToDrop);
                System.out.println("Dropped " + itemToDrop.getName());
                return;
            }

            if (gameState.getCurrentRoom().getIsSafe() && !gameState.safeZoneInventory.inventory.isEmpty()) {
                for (Item item : gameState.safeZoneInventory.inventory) {
                    if (item.getName().equalsIgnoreCase(args[0])) {
                        itemToDrop = item;
                        gameState.safeZoneInventory.removeItemFromInventory(itemToDrop);
                        System.out.println("Dropped " + itemToDrop.getName() + " from safe zone inventory");
                        return;
                    }
                }
                System.out.printf("There is no %s in your inventory or in the safe zone.", args[0]);
            }
        }
    },
    INVENTORY {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (!gameState.getCurrentRoom().getIsSafe()) {
                gameState.getPlayer().listInventory();
            } else {
                System.out.println("Your inventory: ");
                gameState.getPlayer().listInventory();
                System.out.println("");
                System.out.println("Safe Zone inventory: ");
                gameState.safeZoneInventory.listInventory();
            }
        }
    },
    TRANSFER {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (!gameState.getCurrentRoom().getIsSafe()) {
                System.out.println("Return to the safe zone to transfer your items and safely free up some carry weight, or just drop items on the ground here.");
            } else {
                if (args[0].equalsIgnoreCase("all")) {
                    List<Item> playerInventory = gameState.getPlayer().getInventory();
                    double carryWeightDelta = 0;
                    for (Item item : playerInventory) {
                        carryWeightDelta += item.getWeight();
                    }
                    gameState.safeZoneInventory.addListOfItemsToInventory(playerInventory);
                    playerInventory.clear();
                    gameState.getPlayer().setInventory(playerInventory);
                    gameState.getPlayer().reduceRemainingCarryWeight(carryWeightDelta);
                    System.out.println("Transferred your full inventory to the safe zone.");
                } else {
                    List<Item> playerInventory = gameState.getPlayer().getInventory();
                    for (Item item : playerInventory) {
                        if (item.getName().equalsIgnoreCase(args[0])) {
                            gameState.safeZoneInventory.addItemToInventory(item);
                            playerInventory.remove(item);
                            gameState.getPlayer().reduceRemainingCarryWeight(item.getWeight());
                            gameState.getPlayer().setInventory(playerInventory);
                            return;
                        }
                    }
                    System.out.printf("Item of name %s not found in your inventory to transfer.  Use INVENTORY for a list of your items, or TRANSFER ALL to move your entire inventory into the safe zone.", args[0]);
                }
            }
        }
    },
    USE {
        @Override
        public void execute(String[] args, GameState gameState) {
            String itemName = "";

            if (args.length < 1 && gameState.itemContext.equals("")) {
                System.out.println("Enter this command in the format USE [item name]");
                return;
            } else if (args.length < 1) {
                itemName = gameState.itemContext;
            } else {
                itemName = args[0].toLowerCase();
            }

            Item item = gameState.getPlayer().getItemFromInventory(itemName);
            if (item == null && gameState.getCurrentRoom().getIsSafe()) {
                item = gameState.safeZoneInventory.getItemFromInventory(itemName);
            }

            if (item != null) {
                item.use(gameState);
            } else {
                System.out.println("You don't have that item.");
            }
        }
    },
    EQUIP {
        @Override
        public void execute(String[] args, GameState gameState) {
            String itemName = "";

            if (args.length < 1 && gameState.itemContext.equals("")) {
                System.out.println("You need to use this command in the form Equip [Item Name]");
                return;
            } else if (args.length < 1) {
                itemName = gameState.itemContext;
            } else {
                itemName = args[0];
            }

            Item item = gameState.getPlayer().getItemFromInventory(itemName);

            if (item != null && item instanceof Armor) {
                Armor armorItem = (Armor) item;
                armorItem.equip(gameState.getPlayer());
                List<Item> inventory = gameState.getPlayer().getInventory();
                inventory.remove(item);
                gameState.getPlayer().setInventory(inventory);
            } else if (item != null && item instanceof Weapon) {
                Weapon weaponItem = (Weapon) item;
                weaponItem.equip(gameState.getPlayer());
                List<Item> inventory = gameState.getPlayer().getInventory();
                inventory.remove(item);
                gameState.getPlayer().setInventory(inventory);
            } else {
                System.out.println("You don't have \"" + itemName + ",\" or it is not a weapon or armor.");
            }

            gameState.itemContext = "";
        }
    },
    DEQUIP {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length == 0) {
                System.out.println("Specify an item to dequip.");
                return;
            }

            String itemName = args[0].toLowerCase();
            Armor armor = gameState.getPlayer().getArmor();
            Weapon weapon = gameState.getPlayer().getWeapon();

            if (armor == null && weapon == null) {
                System.out.println("You are not wearing any armor or holding any weapon to remove");
                return;
            }

            if (armor.getName().equalsIgnoreCase(itemName)) {
                gameState.getPlayer().removeArmor();
                gameState.getPlayer().addItem(armor);
            } else if (weapon.getName().equalsIgnoreCase(itemName)) {
                gameState.getPlayer().removeWeapon();
                gameState.getPlayer().addItem(weapon);
            } else {
                System.out.println("You are not wearing that armor or weilding that weapon currently.");
            }
        }
    },
    TALK {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                System.out.println("Specify a character to talk to");
            } else {
                String[] arguments = args[0].split(" ");
                if (arguments.length == 0) {
                    System.out.println("Invalid command format.");
                    return;
                }

                if (arguments[0].equalsIgnoreCase("to") && arguments.length > 1) {
                    StringBuilder characterNameBuilder = new StringBuilder();
                    for (int i = 1; i < arguments.length; i++) {
                        characterNameBuilder.append(arguments[i]);
                        if (i < arguments.length - 1) {
                            characterNameBuilder.append(" "); // Add space between words
                        }
                    }
                    String characterName = characterNameBuilder.toString();
                    Collection<NPC> people = gameState.getCurrentRoom().getPeopleInRoom().values();
                    for (NPC person : people) {
                        if (person.getName().equalsIgnoreCase(characterName)) {
                            gameState.enterDialogue(person);
                            return;
                        }
                    }
                    System.out.println(characterName
                            + " was not found in this area. Use LOOK for a list of characters to talk to");
                }
            }
        }
    },
    JOIN {
        @Override
        public void execute(String[] args, GameState gameState) {
            Map<String, NPC> characters = gameState.getCurrentRoom().getPeopleInRoom();
            String memberToJoin = args[0];

            for (Entry<String, NPC> character : characters.entrySet()) {
                if (character.getValue().getName().equalsIgnoreCase(memberToJoin)) {
                    if (character.getValue() instanceof PartyMember) {
                        boolean success = gameState.addPartyMember((PartyMember) character.getValue());
                        if (success) {
                            gameState.getCurrentRoom().removePersonFromRoom(character.getKey());
                            gameState.getCurrentRoom().updateMapEntry('.',
                                    Character.getNumericValue(character.getKey().charAt(1)),
                                    Character.getNumericValue(character.getKey().charAt(3)));
                            return;
                        }
                    } else {
                        System.out.println(
                                "You aren't able to join this person. There are only a few valid party members available in the game, and their \n\t"
                                        +
                                        "presence will be made clear (in the actual game, outside this demo.)");
                    }
                }
            }
        }
    },
    MENU {
        @Override
        public void execute(String[] args, GameState gameState) {
            GameEngine engine = gameState.getGameEngine();
            engine.isInMenu = true;
        }
    },
    LOCATE {
        @Override
        public void execute(String[] args, GameState gameState) {
            // TODO: this will need to be a different format to indicate the overall game
            // region and give more workable information,
            Region region = gameState.getCurrentRegion();
            String safeZone = gameState.getDirectionsToRegion();
            String economicZone = gameState.getDirectionsToEconomicZone();
            System.out.println("You are in " + region);
            System.out.println("The Safe Zone is " + safeZone);
            System.out.println("The Economic Zone is " + economicZone);
        }
    },
    HIDE {
        @Override
        public void execute(String[] args, GameState gameState) {
            gameState.getPlayer().hide();
        }
    },
    FIGHT {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length == 0) {
                System.out.println("Specify who to fight");
                return;
            }

            Collection<NPC> characters = gameState.getCurrentRoom().getPeopleInRoom().values();
            String chosenCharacter = args[0];

            for (Person character : characters) {
                if (character.getName().equalsIgnoreCase(chosenCharacter)) {
                    if ((character instanceof Friend) || (character instanceof PartyMember)) {
                        System.out.println(character.getName() + " does not want to fight");
                        return;
                    } else {
                        double alignmentDelta = (gameState.getPlayer().getAlignment()
                                * (((NPC) character).getAlignmentImpact()));
                        gameState.getPlayer().adjustAlignment(alignmentDelta);
                        gameState.enterCombat(character);
                        return;
                    }
                }
            }
            System.out.println("I don't recognize that name");
        }
    },
    QUIT {
        @Override
        public void execute(String[] args, GameState gameState) {
            // quit logic handled in game engine
            // TODO: this should be replaced by exclusively the menu based quit as i near
            // the end of demo game
        }
    };

    // Abstract method to be implemented by each command
    public abstract void execute(String[] args, GameState gameState);

    // Factory method for parsing input
    public static GameCommand fromString(String command) {
        try {
            return GameCommand.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}