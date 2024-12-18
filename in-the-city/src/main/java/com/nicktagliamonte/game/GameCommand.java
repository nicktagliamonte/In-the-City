package com.nicktagliamonte.game;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Random;

import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Friend;
import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.Neutral;
import com.nicktagliamonte.characters.PartyMember;
import com.nicktagliamonte.characters.Person;
import com.nicktagliamonte.items.Armor;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.items.Plug;
import com.nicktagliamonte.items.Weapon;
import com.nicktagliamonte.rooms.Adjacency;
import com.nicktagliamonte.rooms.Room;

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
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(message.toString());
            gameState.getCurrentRoom().viewAdjascentRooms();
        }
    },
    MOVE {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Which direction do you want to go?");
            } else {
                String[] arguments = args[0].split(" ");
                if (arguments.length == 0) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Invalid command format.");
                    return;
                }

                if (arguments[0].equalsIgnoreCase("to") && arguments.length > 1) {
                    if (gameState.getPlayer().getIsHiding()) {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        System.out.println("Your move speed is reduced while you are hiding. Use UNHIDE, or move just one space at a time.");
                        return;
                    }

                    StringBuilder waypointNameBuilder = new StringBuilder();
                    for (int i = 1; i < arguments.length; i++) {
                        waypointNameBuilder.append(arguments[i]);
                        if (i < arguments.length - 1) {
                            waypointNameBuilder.append(" ");
                        }
                    }
                    String waypointName = waypointNameBuilder.toString();
                    String message = gameState.moveToWaypoint(waypointName);
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(message);
                    return;
                }

                String direction = arguments[0];
                int distance = 1;

                if (arguments.length > 1) {
                    if (!gameState.getPlayer().getIsHiding()) {
                        try {
                            distance = Integer.parseInt(arguments[1]);
                        } catch (NumberFormatException e) {
                            try {
                                Thread.sleep(15);
                            } catch (InterruptedException f) {
                                Thread.currentThread().interrupt();
                            }
                            System.out.println("Invalid number of steps. Please enter a valid integer, or just a direction to move 1 step in that direction.");
                            return;
                        }
                    } else {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        System.out.println("While you are hiding, you can only move one step at a time.  Use UNHIDE to increase your movement speed");
                    }
                }
                String message = gameState.changeLocation(direction, distance);
                if (!(message == null)) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(message);
                }
            }
        }
    },
    ENTER {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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
            String[] argsTokens = args[0].split(" ");
            if (argsTokens.length < 2) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Which room do you want to ascend to?");
                @SuppressWarnings("resource")
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                gameState.ascend(input);
            } else {
                gameState.ascend(argsTokens[1]);
            }
        }
    },
    DESCEND {
        @Override
        public void execute(String[] args, GameState gameState) {
            String[] argsTokens = args[0].split(" ");
            if (argsTokens.length < 2) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Which room do you want to descend to?");
                @SuppressWarnings("resource")
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                gameState.descend(input);
            } else {
                gameState.descend(argsTokens[1]);
            }
        }
    },
    UNLOCK {
        @Override
        public void execute(String[] args, GameState gameState) {
            Random rand = new Random();
            int roll = rand.nextInt(20) + 1;
            double totalDex = roll + gameState.getPlayer().getDexterity();

            if (args.length >= 1) {
                for (Adjacency adj : gameState.getCurrentRoom().getAdjacentRooms()) {
                    if (adj.getAdjoiningRoomName().equalsIgnoreCase(args[0]) && totalDex >= adj.getdexScore()) {
                        gameState.enterCombinationLockSequence(adj.getAdjoiningRoomName(), adj);
                    } else if (adj.getAdjoiningRoomName().equalsIgnoreCase(args[0])) {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        System.out.println("You cannot reach that lock.");
                    }
                }
            } else {
                gameState.getPlayer().hasKey(gameState.getCurrentRoom().getAdjacentRooms());
            }
        }
    },
    LOCKPICK {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Specify a room to lockpick");
                return;
            }

            Random rand = new Random();
            int roll = rand.nextInt(20) + 1;
            double totalDex = roll + gameState.getPlayer().getDexterity();

            String targetRoomName = args[0];
            List<Adjacency> adjacentRooms = gameState.getCurrentRoom().getAdjacentRooms();

            boolean roomFound = false;

            for (Adjacency adjacency : adjacentRooms) {
                if (adjacency.getAdjoiningRoomName().equalsIgnoreCase(targetRoomName) && adjacency.getIsLocked() && adjacency.getLockType().equalsIgnoreCase("pickable") && totalDex >= adjacency.getdexScore()) {
                    roomFound = true;
                    gameState.startLockpickingSequence(targetRoomName, adjacency);
                    return;
                } else if (adjacency.getLockType().equalsIgnoreCase("combination")) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(targetRoomName + " requires a combination to unlock, you cannot pick it.");
                    return;
                } else if (adjacency.getAdjoiningRoomName().equalsIgnoreCase(targetRoomName)) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(targetRoomName + " is already unlocked. You can enter it by typing \"ENTER " + targetRoomName + "\"");
                    return;
                } else if (totalDex < adjacency.getdexScore()) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("You cannot access that lock to pick it.");
                }
            }

            if (!roomFound) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("There is no adjoining room named " + targetRoomName);
            }
        }
    },
    EXAMINE {
        @Override
        public void execute(String[] args, GameState gameState) {
            String itemName = "";

            if (args.length < 1 && gameState.itemContext.equals("")) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You need to use this command in the form Examine [Item Name]");
                return;
            } else if (args.length < 1) {
                itemName = gameState.itemContext;
            } else {
                itemName = args[0];
            }

            List<Item> itemsInRoom = new ArrayList<>(gameState.getCurrentRoom().getItemsInRoom().values());
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

            if (itemToExamine != null && (itemToExamine.getPuzzleType().equals(""))) {
                System.out.println(itemToExamine.getDescription());
                if (itemToExamine instanceof Plug) {
                    itemToExamine.use(gameState);
                }
            } else if (itemToExamine != null && (itemToExamine.getPuzzleType().equalsIgnoreCase("sequence"))) {
                if (itemToExamine.getInteractable()) {
                    gameState.launchSequencePuzzle(itemToExamine.getDataPath());
                } else {
                    System.out.println(itemToExamine.getDescription());
                }                
            } else if (itemToExamine != null && (itemToExamine.getPuzzleType().equalsIgnoreCase("mastermind"))) {
                gameState.launchMastermindPuzzle(itemToExamine.getDataPath());
            }
            else {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        System.out.println("That item is too heavy.");
                    }
                    return;
                } else {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
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
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("That item is too heavy.");
                }
            } else {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("There is no such item here: " + itemName);
            }
        }
    },
    DROP {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You need to specify an item to drop");
                return;
            }

            Item itemToDrop = null;
            List<Item> itemsInInventory = gameState.getPlayer().getInventory();

            if (itemsInInventory == null && !gameState.getCurrentRoom().getIsSafe()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Dropped " + itemToDrop.getName());
                return;
            }

            if (gameState.getCurrentRoom().getIsSafe() && !gameState.safeZoneInventory.inventory.isEmpty()) {
                for (Item item : gameState.safeZoneInventory.inventory) {
                    if (item.getName().equalsIgnoreCase(args[0])) {
                        itemToDrop = item;
                        gameState.safeZoneInventory.removeItemFromInventory(itemToDrop);
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
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
                System.out.println("\nYour Inventory");
                System.out.println("──────────────────────");
                gameState.getPlayer().listInventory();
                System.out.println("Remaining Carry Weight: " + gameState.getPlayer().getRemainingCarryWeight());
                
            } else {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("\nYour inventory: ");
                System.out.println("──────────────────────");
                gameState.getPlayer().listInventory();
                System.out.println("Remaining Carry Weight: " + gameState.getPlayer().getRemainingCarryWeight());             
                System.out.println("");
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Safe Zone inventory: ");
                gameState.safeZoneInventory.listInventory();
            }
        }
    },
    TRANSFER {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (!gameState.getCurrentRoom().getIsSafe()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
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
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You don't have that item.");
            }
        }
    },
    EQUIP {
        @Override
        public void execute(String[] args, GameState gameState) {
            String itemName = "";

            if (args.length < 1 && gameState.itemContext.equals("")) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You don't have \"" + itemName + ",\" or it is not a weapon or armor.");
            }

            gameState.itemContext = "";
        }
    },
    DEQUIP {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length == 0) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Specify an item to dequip.");
                return;
            }

            String itemName = args[0].toLowerCase();
            Armor armor = gameState.getPlayer().getArmor();
            Weapon weapon = gameState.getPlayer().getWeapon();

            if (armor == null && weapon == null) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You are not wearing that armor or weilding that weapon currently.");
            }
        }
    },
    TALK {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Specify a character to talk to");
            } else {
                String[] arguments = args[0].split(" ");
                if (arguments.length == 0) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
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
                            try {
                                gameState.enterDialogue(person);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(characterName
                            + " was not found in this area. Use LOOK for a list of characters to talk to");
                } else {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("This command has to be formatted as talk TO [character name]");
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
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        System.out.println("You aren't able to join this person. There are only a few valid party members available in the game, and their \n\t" +
                                        "presence will be made clear (in the actual game, outside this demo. In the demo, the only valid character to join is Sam.)");
                    }
                }
            }
        }
    },
    ROB {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Specify a character to rob");
            } else {
                List<Item> characterInventory = null;
                NPC characterToRob = null;

                //get the character to rob
                Collection<NPC> people = gameState.getCurrentRoom().getPeopleInRoom().values();
                for (NPC person : people) {
                    if (person.getName().equalsIgnoreCase(args[0])) {
                        characterToRob = person;

                        //can't rob party members
                        if (characterToRob instanceof PartyMember) {
                            try {
                                Thread.sleep(15);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            System.out.println("You cannot rob " + characterToRob.getName());
                            return;
                        } else {
                            //increase alignment for robbing bad guys, decrease for robbing good guys. reduce the size of change relative to combat interaction
                            double alignmentDelta = (gameState.getPlayer().getAlignment() * characterToRob.getAlignmentImpact());
                            alignmentDelta *= 0.8;
                            gameState.getPlayer().adjustAlignment(alignmentDelta);
                        }

                        //determine theivery success with opposed wis/char checks (benefitting the negotiator class)
                        double playerScore = gameState.getPlayer().getWisdom() * 1.5
                                        + gameState.getPlayer().getCharisma()
                                        + (Math.random() * 10);
                                    
                        double npcScore = characterToRob.getWisdom() * 1.5
                                        + characterToRob.getCharisma()
                                        + Math.random() * 10;
                        
                        //respond to failure depending on the type of character you're attempting to rob
                        if (playerScore < npcScore) {
                            try {
                                Thread.sleep(15);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            System.out.println("Your attempt to rob " + characterToRob.getName() + " failed.");
                            if (characterToRob instanceof Adversary || characterToRob instanceof Neutral) {
                                gameState.getPlayer().gainXP(5, gameState);
                                gameState.enterCombat(characterToRob);
                            } else if (characterToRob instanceof Friend) {
                                try {
                                    Thread.sleep(15);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                                System.out.println("You feel that " + characterToRob.getName() + " will detect your presence if you continue, and you do not want to betray their trust");
                            }
                            return;
                        }

                        //determine character's inventory contents
                        if (characterToRob.getInventory().isEmpty() || characterToRob.getInventory() == null) {
                            try {
                                Thread.sleep(15);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            System.out.println(person.getName() + " does not have anything you can steal");
                            return;
                        }
                        characterInventory = person.getInventory();
                    }
                }

                //get highest value item to take and remove it from the character
                Item itemToTake = null;
                int highestValue = 0;
                for (Item item : characterInventory) {
                    if (item.getValue() > highestValue) {
                        itemToTake = item;
                    }
                }
                characterInventory.remove(itemToTake);
                characterToRob.setInventory(characterInventory);

                //add the item to your inventory if you can hold it, otherwise put it in the safe zone
                if (gameState.getPlayer().getRemainingCarryWeight() > itemToTake.getWeight()) {
                    gameState.getPlayer().addItem(itemToTake);
                    gameState.getPlayer().reduceRemainingCarryWeight(itemToTake.getWeight());
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("You successfully took " + itemToTake.getName() + " from " + characterToRob.getName());
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(itemToTake.getName() + " was added to your inventory.");
                } else {
                    gameState.safeZoneInventory.addItemToInventory(itemToTake);
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("You successfully took " + itemToTake.getName() + " from " + characterToRob.getName());
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(itemToTake.getName() + " was added to the safe zone inventory, because you do not have enough remaining carry weight.");
                }
                gameState.getPlayer().gainXP(20, gameState);
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
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You are in the region \"" + gameState.getCurrentRegion().getRegionName() + "\"");
    
            if (!gameState.getCurrentRegion().getHasSafeZone()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("There is not (yet) a safe zone in this region.");
                return;
            }
    
            Room currentRoom = gameState.getCurrentRoom();
            Region currentRegion = gameState.getCurrentRegion();
    
            if (currentRoom.getIsSafe()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("You are already in the safe zone.");
                return;
            }
    
            StringBuilder pathToSafeZone = new StringBuilder("To get to the safe zone from your current position, go through: ");
            Room nextRoom = currentRoom;
    
            while (!nextRoom.getIsSafe()) {
                String nextRoomName = nextRoom.getNextRoomToSafeZone();
                if (nextRoomName == null) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Cannot find a path to the safe zone from your current position.");
                    return;
                }
                pathToSafeZone.append("\"").append(nextRoomName).append("\"");
    
                nextRoom = currentRegion.getRooms().stream()
                    .filter(room -> room.getName().equalsIgnoreCase(nextRoomName))
                    .findFirst()
                    .orElse(null);
    
                if (nextRoom == null) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Error: Path is incomplete or invalid.");
                    return;
                }
    
                if (!nextRoom.getIsSafe()) {
                    pathToSafeZone.append(" -> ");
                }
            }
    
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(pathToSafeZone);
        }
    },
    HIDE {
        @Override
        public void execute(String[] args, GameState gameState) {
            Random random = new Random();
            int chance = random.nextInt(10);
            if (chance == 0) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Hide attempt failed.");
            } else {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Successfully hid.");
                gameState.getPlayer().setIsHiding(true);
            }
        }
    },
    UNHIDE {
        @Override
        public void execute(String[] args, GameState gameState) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("You are no longer hiding.");
            gameState.getPlayer().setIsHiding(false);
        }
    },
    FIGHT {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length == 0) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Specify who to fight");
                return;
            }

            Collection<NPC> characters = gameState.getCurrentRoom().getPeopleInRoom().values();
            String chosenCharacter = args[0];

            for (Person character : characters) {
                if (character.getName().equalsIgnoreCase(chosenCharacter)) {
                    if ((character instanceof Friend) || (character instanceof PartyMember)) {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
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
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("I don't recognize that name");
        }
    },
    QUIT {
        @Override
        public void execute(String[] args, GameState gameState) {
            // quit logic handled in game engine
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