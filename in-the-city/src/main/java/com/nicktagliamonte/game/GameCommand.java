package com.nicktagliamonte.game;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Random;

import com.nicktagliamonte.characters.Adversary;
import com.nicktagliamonte.characters.Conveyance;
import com.nicktagliamonte.characters.Friend;
import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.Neutral;
import com.nicktagliamonte.characters.PartyMember;
import com.nicktagliamonte.characters.Person;
import com.nicktagliamonte.characters.Player;
import com.nicktagliamonte.items.Amulet;
import com.nicktagliamonte.items.Armor;
import com.nicktagliamonte.items.Belt;
import com.nicktagliamonte.items.Boots;
import com.nicktagliamonte.items.Gloves;
import com.nicktagliamonte.items.HeadBand;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.items.Plug;
import com.nicktagliamonte.items.Ring;
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
            Map<String, List<Item>> visibleItems = gameState.getCurrentRoom().getItemsInRoom();
            Map<String, NPC> characters = gameState.getCurrentRoom().getPeopleInRoom();
            List<NPC> party = gameState.getCurrentParty();
    
            // Construct the dynamic message based on the game state
            StringBuilder message = new StringBuilder();
            message.append("You look around and see: \n").append(roomDescription).append("\n");
            if (gameState.getCurrentRoom().getIsSafe()) {
                message.append("This is the region's safe zone. There will be no combat events here unless you initiate them.\n");
            }
    
            // Handle items in the room with quantity check and coordinates display
            if (!visibleItems.isEmpty()) {
                message.append("Items here: ");
                boolean firstItem = true;
                Map<String, Integer> itemCounts = new HashMap<>();
    
                // Count occurrences of each item and collect unique coordinates
                Map<String, Set<String>> itemCoordinates = new HashMap<>();
                for (Map.Entry<String, List<Item>> entry : visibleItems.entrySet()) {
                    for (Item item : entry.getValue()) {
                        itemCounts.put(item.getName(), itemCounts.getOrDefault(item.getName(), 0) + 1);
    
                        // Collect unique coordinates for each item
                        itemCoordinates.computeIfAbsent(item.getName(), _ -> new HashSet<>()).add(entry.getKey());
                    }
                }
    
                // Build the message with item quantities and unique coordinates
                for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
                    if (!firstItem) {
                        message.append(", ");
                    }
                    int quantity = entry.getValue();
                    String itemName = entry.getKey();
    
                    // Get the unique coordinates for the item
                    Set<String> coordinates = itemCoordinates.get(itemName);
                    String coordinatesStr = String.join(", ", coordinates);
    
                    message.append(quantity).append(" ").append(itemName).append((quantity > 1) ? "s" : "")
                            .append(" at ").append(coordinatesStr);
                    firstItem = false;
                }
                message.append("\n");
            } else {
                message.append("There are no notable items here.\n");
            }
    
            // Handle characters in the room
            if (!characters.isEmpty()) {
                message.append("People here: ");
                boolean firstCharacter = true;
                for (Map.Entry<String, NPC> entry : characters.entrySet()) {
                    if (!firstCharacter) {
                        message.append(", ");
                    }
                    message.append(entry.getValue().getName())
                            .append(" at ")
                            .append(entry.getKey());
                    firstCharacter = false;
                }
                message.append("\n");
            } else {
                message.append("There are no notable people here.\n");
            }
    
            // Handle party members
            if (!party.isEmpty()) {
                message.append("Party members here: ");
                boolean firstPartyMember = true;
                for (NPC partyMember : party) {
                    if (!firstPartyMember) {
                        message.append(", ");
                    }
                    message.append(partyMember.getName());
                    firstPartyMember = false;
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
                        System.out.println("While you are hiding, you can only move one step at a time. Use UNHIDE to increase your movement speed.");
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
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
    
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
        @SuppressWarnings("resource")
        @Override
        public void execute(String[] args, GameState gameState) {
            String targetRoom = "";
    
            // Check if args is empty or if the first token is "to"
            if (args.length == 0) {
                System.out.println("Which room do you want to ascend to?");
                Scanner scanner = new Scanner(System.in);
                targetRoom = scanner.nextLine();
            } else {
                String[] argTokens = String.join(" ", args).split(" "); // Tokenize by spaces
                if (argTokens[0].equalsIgnoreCase("to") && argTokens.length > 1) {
                    // Join the remaining tokens to form the room name
                    targetRoom = String.join(" ", Arrays.copyOfRange(argTokens, 1, argTokens.length));
                } else {
                    targetRoom = String.join(" ", argTokens);
                }
            }
    
            gameState.ascend(targetRoom);
        }
    },
    DESCEND {
        @SuppressWarnings("resource")
        @Override
        public void execute(String[] args, GameState gameState) {
            String targetRoom = "";
    
            // Check if args is empty or if the first token is "to"
            if (args.length == 0) {
                System.out.println("Which room do you want to descend to?");
                Scanner scanner = new Scanner(System.in);
                targetRoom = scanner.nextLine();
            } else {
                String[] argTokens = String.join(" ", args).split(" "); // Tokenize by spaces
                if (argTokens[0].equalsIgnoreCase("to") && argTokens.length > 1) {
                    // Join the remaining tokens to form the room name
                    targetRoom = String.join(" ", Arrays.copyOfRange(argTokens, 1, argTokens.length));
                } else {
                    targetRoom = String.join(" ", argTokens);
                }
            }
    
            gameState.descend(targetRoom);
        }
    },        
    UNLOCK {
        @Override
        public void execute(String[] args, GameState gameState) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
    
            Random rand = new Random();
            int roll = rand.nextInt(20) + 1;
            double totalDex = roll + gameState.getPlayer().getDexterity();
    
            if (args.length >= 1) {
                boolean lockFound = false;
                for (Adjacency adj : gameState.getCurrentRoom().getAdjacentRooms()) {
                    if (adj.getAdjoiningRoomName().equalsIgnoreCase(args[0])) {
                        lockFound = true;
                        if (totalDex >= adj.getdexScore()) {
                            gameState.enterCombinationLockSequence(adj.getAdjoiningRoomName(), adj);
                        } else {
                            System.out.println("You cannot reach that lock.");
                        }
                        break;
                    }
                }
                if (!lockFound) {
                    System.out.println("The specified room is not adjacent.");
                }
            } else {
                gameState.getPlayer().hasKey(gameState.getCurrentRoom().getAdjacentRooms());
            }
        }
    },    
    LOCKPICK {
        @Override
        public void execute(String[] args, GameState gameState) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
    
            if (args.length < 1) {
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
                if (adjacency.getAdjoiningRoomName().equalsIgnoreCase(targetRoomName)) {
                    if (adjacency.getIsLocked()) {
                        if (adjacency.getLockType().equalsIgnoreCase("pickable") && totalDex >= adjacency.getdexScore()) {
                            roomFound = true;
                            gameState.startLockpickingSequence(targetRoomName, adjacency);
                            return;
                        } else if (adjacency.getLockType().equalsIgnoreCase("combination")) {
                            System.out.println(targetRoomName + " requires a combination to unlock, you cannot pick it.");
                            return;
                        } else if (adjacency.getLockType().equalsIgnoreCase("unpickable")) {
                            System.out.println("This lock cannot be picked.");
                            return;
                        } else if (totalDex < adjacency.getdexScore()) {
                            System.out.println("You cannot access that lock to pick it.");
                            return;
                        }
                    } else {
                        System.out.println(targetRoomName + " is already unlocked. You can enter it by typing \"ENTER " + targetRoomName + "\"");
                        return;
                    }
                }
            }
    
            if (!roomFound) {
                System.out.println("There is no adjoining room named " + targetRoomName);
            }
        }
    },    
    EXAMINE {
        @Override
        public void execute(String[] args, GameState gameState) {
            String itemName = "";
    
            if (args.length < 1 && gameState.itemContext.equals("")) {
                System.out.println("You need to use this command in the form Examine [Item/Person Name]");
                return;
            } else if (args.length < 1) {
                itemName = gameState.itemContext;
            } else {
                itemName = args[0];
            }
    
            // Collect all items from the current room
            List<Item> itemsInRoom = new ArrayList<>();
            for (List<Item> itemsList : gameState.getCurrentRoom().getItemsInRoom().values()) {
                itemsInRoom.addAll(itemsList); // Add all items in each list to the final list
            }
    
            // Add items from the safe zone if it's a safe zone, and conveyance if you have conveyance
            boolean hasConveyance = false;
            boolean conveyanceHunger = false;
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    hasConveyance = true;
                    if (((Conveyance) npc).getInHunger()) {
                        conveyanceHunger = true;
                        System.out.println("The conveyance is low on fuel and cannot access safe zone inventory currently. Use a Fuel Cell to resolve this.");
                    }

                }
            }
            if (gameState.getCurrentRoom().getIsSafe() || (hasConveyance && !conveyanceHunger)) {
                itemsInRoom.addAll(gameState.safeZoneInventory.getInventory());
            }
    
            // Find the item to examine
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
                    return;
                }
            } else if (itemToExamine != null && (itemToExamine.getPuzzleType().equalsIgnoreCase("sequence"))) {
                if (itemToExamine.getInteractable()) {
                    gameState.launchSequencePuzzle(itemToExamine.getDataPath());
                } else {
                    System.out.println(itemToExamine.getDescription());
                    return;
                }                
            } else if (itemToExamine != null && (itemToExamine.getPuzzleType().equalsIgnoreCase("mastermind"))) {
                gameState.launchMastermindPuzzle(itemToExamine.getDataPath());
            }
    
            // Check for NPCs in the room
            List<NPC> peopleInRoom = new ArrayList<>(gameState.getCurrentRoom().getPeopleInRoom().values());
    
            boolean personFound = false;
            for (NPC person : peopleInRoom) {
                if (person.getName().equalsIgnoreCase(itemName)) {
                    person.getDescription();
                    personFound = true;
                    break;
                }
            }
    
            // If no item or person was found, print the message
            if (itemToExamine == null && !personFound) {
                System.out.println("There is no such item here: " + itemName);
            }
    
            // Clean up
            itemsInRoom.clear();
            peopleInRoom.clear();
            gameState.itemContext = "";
        }
    },   
    TAKE {
        @Override
        public void execute(String[] args, GameState gameState) {
            String itemName = "";
    
            // Determine item name
            if (args.length < 1 && gameState.itemContext.equals("")) {
                System.out.println("You need to use this command in the form TAKE [Item Name]");
                return;
            } else {
                itemName = (args.length < 1) ? gameState.itemContext : args[0];
            }
    
            Item itemToTake = null;
    
            // Check if we're in a safe zone or have conveyance
            boolean hasConveyance = false;
            boolean conveyanceHunger = false;
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    hasConveyance = true;
                    if (((Conveyance) npc).getInHunger()) {
                        conveyanceHunger = true;
                        System.out.println("The conveyance is low on fuel and cannot access safe zone inventory currently. Use a Fuel Cell to resolve this.");
                    }

                }
            }
            if (gameState.getCurrentRoom().getIsSafe() || (hasConveyance && !conveyanceHunger)) {
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
                }
            }
    
            // Check items in the current room
            Map<String, List<Item>> itemsInRoom = gameState.getCurrentRoom().getItemsInRoom();
            String itemLocation = null;
    
            // Iterate through the room's items to find the item
            for (Map.Entry<String, List<Item>> entry : itemsInRoom.entrySet()) {
                for (Item item : entry.getValue()) {
                    if (item.getName().equalsIgnoreCase(itemName)) {
                        itemToTake = item;
                        itemLocation = entry.getKey();
                        gameState.getCurrentRoom().updateMapEntry('.', 
                                Character.getNumericValue(itemLocation.charAt(1)), 
                                Character.getNumericValue(itemLocation.charAt(3)));
                        entry.getValue().remove(item);  // Remove item from room
                        break;
                    }
                }
                if (itemToTake != null) break; // If found, exit outer loop
            }
    
            // Handle taking the item if found
            if (itemToTake != null) {
                if (gameState.getPlayer().getRemainingCarryWeight() > itemToTake.getWeight()) {
                    gameState.getPlayer().addItem(itemToTake);
                    gameState.getPlayer().reduceRemainingCarryWeight(itemToTake.getWeight());
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
                displayMessageWithDelay("You need to specify an item to drop");
                return;
            }
    
            String itemName = args[0];
            Player player = gameState.getPlayer();
            Room currentRoom = gameState.getCurrentRoom();
            safeZoneInventory safeZoneInventory = gameState.safeZoneInventory;
    
            // Attempt to drop item from player's inventory
            Item itemToDrop = findItemByName(player.getInventory(), itemName);
    
            if (itemToDrop != null) {
                dropItemFromInventory(player, currentRoom, itemToDrop);
                return;
            }
    
            // Handle case when inventory is empty or item not found
            if (!currentRoom.getIsSafe()) {
                displayMessageWithDelay("There is no " + itemName + " in your inventory");
                return;
            }
    
            // Attempt to drop item from the safe zone or conveyance inventory
            boolean hasConveyance = false;
            boolean conveyanceHunger = false;
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    hasConveyance = true;
                    if (((Conveyance) npc).getInHunger()) {
                        conveyanceHunger = true;
                        System.out.println("The conveyance is low on fuel and cannot access safe zone inventory currently. Use a Fuel Cell to resolve this.");
                    }

                }
            }
            if (gameState.getCurrentRoom().getIsSafe() || (hasConveyance && !conveyanceHunger)) {
                if (safeZoneInventory != null) {
                    itemToDrop = findItemByName(safeZoneInventory.inventory, itemName);
                    if (itemToDrop != null) {
                        safeZoneInventory.removeItemFromInventory(itemToDrop);
                        displayMessageWithDelay("Dropped " + itemToDrop.getName() + " from safe zone inventory");
                        return;
                    }
                }
            }
    
            // Final message if the item is not found anywhere
            displayMessageWithDelay("There is no " + itemName + " in your inventory or in the safe zone.");
        }
    
        // Helper method to find an item by name in a list
        private Item findItemByName(List<Item> items, String name) {
            if (items == null) return null;
            for (Item item : items) {
                if (item.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            }
            return null;
        }
    
        // Helper method to handle dropping an item from the player's inventory
        private void dropItemFromInventory(Player player, Room room, Item item) {
            player.removeItemFromInventory(item);
            room.addItemToRoom(room.getPlayerPosition(), item);
            room.updateMapEntry('I',
                    Character.getNumericValue(room.getPlayerPosition().charAt(1)),
                    Character.getNumericValue(room.getPlayerPosition().charAt(4)));
            displayMessageWithDelay("Dropped " + item.getName());
        }
    
        // Helper method to display a message with a delay
        private void displayMessageWithDelay(String message) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(message);
        }
    },    
    INVENTORY {
        @Override
        public void execute(String[] args, GameState gameState) {
            boolean hasConveyance = false;
            boolean conveyanceHunger = false;
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    hasConveyance = true;
                    if (((Conveyance) npc).getInHunger()) {
                        conveyanceHunger = true;
                        System.out.println("The conveyance is low on fuel and cannot access safe zone inventory currently. Use a Fuel Cell to resolve this.");
                    }

                }
            }
            if ((!gameState.getCurrentRoom().getIsSafe() && !hasConveyance) || conveyanceHunger) {
                System.out.println("\nYour Inventory");
                System.out.println("──────────────────────");
                gameState.getPlayer().listInventory();
                System.out.println("Remaining Carry Weight: " + gameState.getPlayer().getRemainingCarryWeight());                
            } else {
                System.out.println("\nYour inventory: ");
                System.out.println("──────────────────────");
                gameState.getPlayer().listInventory();
                System.out.println("Remaining Carry Weight: " + gameState.getPlayer().getRemainingCarryWeight());             
                System.out.println("");
                System.out.println("Safe Zone inventory: ");
                gameState.safeZoneInventory.listInventory();
            }
        }
    },
    TRANSFER {
        @Override
        public void execute(String[] args, GameState gameState) {
            boolean hasConveyance = false;
            boolean conveyanceHunger = false;
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    hasConveyance = true;
                    if (((Conveyance) npc).getInHunger()) {
                        conveyanceHunger = true;
                        System.out.println("The conveyance is low on fuel and cannot access safe zone inventory currently. Use a Fuel Cell to resolve this.");
                    }

                }
            }

            if ((!gameState.getCurrentRoom().getIsSafe() && !hasConveyance) || conveyanceHunger) {
                displayMessageWithDelay("Return to the safe zone to transfer your items and safely free up some carry weight, or just drop items on the ground here.");
                return;
            }
    
            if (args.length < 1) {
                displayMessageWithDelay("Specify an item to transfer or use 'ALL' to transfer everything.");
                return;
            }
    
            String itemName = args[0];
            
            if (itemName.equalsIgnoreCase("all")) {
                transferAllItemsToSafeZone(gameState);
            } else {
                transferSingleItemToSafeZone(gameState, itemName);
            }
        }
    
        // Helper method to display a message with a delay
        private void displayMessageWithDelay(String message) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(message);
        }
    
        // Helper method to transfer all items to the safe zone
        private void transferAllItemsToSafeZone(GameState gameState) {
            List<Item> playerInventory = gameState.getPlayer().getInventory();
            double carryWeightDelta = playerInventory.stream().mapToDouble(Item::getWeight).sum();
    
            gameState.safeZoneInventory.addListOfItemsToInventory(playerInventory);
            playerInventory.clear();
            gameState.getPlayer().setInventory(playerInventory);
            gameState.getPlayer().reduceRemainingCarryWeight(carryWeightDelta);
    
            displayMessageWithDelay("Transferred your full inventory to the safe zone.");
        }
    
        // Helper method to transfer a single item to the safe zone
        private void transferSingleItemToSafeZone(GameState gameState, String itemName) {
            List<Item> playerInventory = gameState.getPlayer().getInventory();
            Item itemToTransfer = findItemByName(playerInventory, itemName);
    
            if (itemToTransfer != null) {
                gameState.safeZoneInventory.addItemToInventory(itemToTransfer);
                gameState.getPlayer().removeItemFromInventory(itemToTransfer);
                displayMessageWithDelay("Transferred " + itemToTransfer.getName() + " to the safe zone.");
            } else {
                displayMessageWithDelay("Item of name " + itemName + " not found in your inventory to transfer. Use INVENTORY for a list of your items, or TRANSFER ALL to move your entire inventory into the safe zone.");
            }
        }
    
        // Helper method to find an item by name in a list
        private Item findItemByName(List<Item> items, String name) {
            return items.stream()
                    .filter(item -> item.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);
        }
    },    
    USE {
        @Override
        public void execute(String[] args, GameState gameState) {
            String itemName = resolveItemName(args, gameState);
    
            if (itemName == null) {
                displayMessageWithDelay("Enter this command in the format USE [item name]");
                return;
            }

            if (itemName.equalsIgnoreCase("conveyance")) {
                System.out.println("You regard the conveyance. Or the two of you regard one another. Little else happens.");
            }
    
            Item item = findItem(itemName, gameState);
    
            if (item != null) {
                useItem(item, gameState);
            } else {
                displayMessageWithDelay("You don't have that item.");
            }
        }
    
        // Helper method to resolve item name from args or itemContext
        private String resolveItemName(String[] args, GameState gameState) {
            if (args.length < 1 && gameState.itemContext.equals("")) {
                return null; // Item name is required
            }
            return args.length < 1 ? gameState.itemContext.toLowerCase() : args[0].toLowerCase();
        }
    
        // Helper method to find an item in either player inventory or safe zone inventory
        private Item findItem(String itemName, GameState gameState) {
            boolean hasConveyance = false;
            boolean conveyanceHunger = false;
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    hasConveyance = true;
                    if (((Conveyance) npc).getInHunger()) {
                        conveyanceHunger = true;
                        System.out.println("The conveyance is low on fuel and cannot access safe zone inventory currently. Use a Fuel Cell to resolve this.");
                    }

                }
            }
            Item item = gameState.getPlayer().getItemFromInventory(itemName);
            if (item == null && (gameState.getCurrentRoom().getIsSafe() || (hasConveyance && !conveyanceHunger))) {
                item = gameState.safeZoneInventory.getItemFromInventory(itemName);
            }
            return item;
        }
    
        // Helper method to handle item usage
        private void useItem(Item item, GameState gameState) {
            if (item.getIsConsumable()) {
                gameState.getPlayer().removeItemFromInventory(item);
            }
            item.use(gameState);
        }
    
        // Helper method to display messages with delay
        private void displayMessageWithDelay(String message) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(message);
        }
    },    
    EAT {
        @Override
        public void execute(String[] args, GameState gameState) {
            boolean hasConveyance = false;
            boolean conveyanceHunger = false;
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    hasConveyance = true;
                    if (((Conveyance) npc).getInHunger()) {
                        conveyanceHunger = true;
                        System.out.println("The conveyance is low on fuel and cannot access safe zone inventory currently. Use a Fuel Cell to resolve this.");
                    }

                }
            }

            List<Item> inventory = gameState.getPlayer().getInventory();
            if (gameState.getCurrentRoom().getIsSafe() || (hasConveyance && !conveyanceHunger)) {
                inventory.addAll(gameState.safeZoneInventory.getInventory());
            }

            boolean foodFound = false;
            for (Item item : inventory) {
                if (item.getName().equalsIgnoreCase("food ration")) {
                    item.use(gameState);
                    foodFound = true;
                    break;
                }
            }
            if (!foodFound) {
                System.out.println("You don't have any food to eat. Find some, or set a trap to catch some.");
            }
        }
    },
    DRINK {
        @Override
        public void execute(String[] args, GameState gameState) {
            boolean hasConveyance = false;
            boolean conveyanceHunger = false;
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    hasConveyance = true;
                    if (((Conveyance) npc).getInHunger()) {
                        conveyanceHunger = true;
                        System.out.println("The conveyance is low on fuel and cannot access safe zone inventory currently. Use a Fuel Cell to resolve this.");
                    }

                }
            }
            
            List<Item> inventory = gameState.getPlayer().getInventory();
            if (gameState.getCurrentRoom().getIsSafe() || (hasConveyance && !conveyanceHunger)) {
                inventory.addAll(gameState.safeZoneInventory.getInventory());
            }

            boolean waterFound = false;
            for (Item item : inventory) {
                if (item.getName().equalsIgnoreCase("water")) {
                    item.use(gameState);
                    waterFound = true;
                    break;
                }
            }
            if (!waterFound) {
                System.out.println("You don't have any water to drink. Find some, or set a trap to catch some.");
            }
        }
    },    
    EQUIP {
        @Override
        public void execute(String[] args, GameState gameState) {
            // Determine the item name
            String itemName = (args.length < 1) ? gameState.itemContext : args[0];
            
            if (itemName.isEmpty()) {
                System.out.println("You need to use this command in the form Equip [Item Name]");
                return;
            }

            // Attempt to get the item from the player's inventory
            Item item = gameState.getPlayer().getItemFromInventory(itemName);

            // Consider the conveyance or safe zone inventory
            boolean hasConveyance = false;
            boolean conveyanceHunger = false;
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    hasConveyance = true;
                    if (((Conveyance) npc).getInHunger()) {
                        conveyanceHunger = true;
                        System.out.println("The conveyance is low on fuel and cannot access safe zone inventory currently. Use a Fuel Cell to resolve this.");
                    }

                }
            }
            if (item == null && (gameState.getCurrentRoom().getIsSafe() || (hasConveyance && !conveyanceHunger))) {
                item = gameState.safeZoneInventory.getItemFromInventory(itemName);
            }

            if (item instanceof Armor armor) {
                if (gameState.getPlayer().getLevel() >= armor.getMinLevel()) {
                    armor.equip(gameState.getPlayer());
                } else {
                    System.out.println("You are not high enough level to equip this armor.");
                }
            } else if (item instanceof Weapon weapon) {
                weapon.equip(gameState.getPlayer());
            } else if (item instanceof Amulet amulet) {
                if (gameState.getPlayer().getLevel() >= amulet.getMinLevel()) {
                    amulet.equip(gameState.getPlayer());
                } else {
                    System.out.println("You are not high enough level to equip this amulet.");
                }
            } else if (item instanceof Belt belt) {
                if (gameState.getPlayer().getLevel() >= belt.getMinLevel()) {
                    belt.equip(gameState.getPlayer());
                } else {
                    System.out.println("You are not high enough level to equip this belt.");
                }
            } else if (item instanceof Boots boots) {
                if (gameState.getPlayer().getLevel() >= boots.getMinLevel()) {
                    boots.equip(gameState.getPlayer());
                } else {
                    System.out.println("You are not high enough level to equip these boots.");
                }
            } else if (item instanceof Gloves gloves) {
                if (gameState.getPlayer().getLevel() >= gloves.getMinLevel()) {
                    gloves.equip(gameState.getPlayer());
                } else {
                    System.out.println("You are not high enough level to equip these gloves.");
                }
            } else if (item instanceof HeadBand headBand) {
                if (gameState.getPlayer().getLevel() >= headBand.getMinLevel()) {
                    headBand.equip(gameState.getPlayer());
                } else {
                    System.out.println("You are not high enough level to equip this headband.");
                }
            } else if (item instanceof Ring ring) {
                if (gameState.getPlayer().getLevel() >= ring.getMinLevel()) {
                    ring.equip(gameState.getPlayer());
                } else {
                    System.out.println("You are not high enough level to equip this ring.");
                }
            } else {
                System.out.println("You don't have \"" + itemName + "\" or it is not an equippable item.");
            }

            // Clear the item context
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
            Player player = gameState.getPlayer();
    
            Armor armor = player.getArmor();
            Weapon weapon = player.getWeapon();
            Amulet amulet = player.getAmulet();
            Belt belt = player.getBelt();
            Boots boots = player.getBoots();
            Gloves gloves = player.getGloves();
            HeadBand headBand = player.getHeadBand();
            Ring ring = player.getRing();
    
            // Check and dequip each item type
            if (armor != null && armor.getName().equalsIgnoreCase(itemName)) {
                player.removeArmor();
                player.addItem(armor);
                System.out.println("Successfully dequipped " + armor.getName() + ".");
            } else if (weapon != null && weapon.getName().equalsIgnoreCase(itemName)) {
                player.removeWeapon();
                player.addItem(weapon);
                System.out.println("Successfully dequipped " + weapon.getName() + ".");
            } else if (amulet != null && amulet.getName().equalsIgnoreCase(itemName)) {
                player.removeAmulet();
                player.addItem(amulet);
                System.out.println("Successfully dequipped " + amulet.getName() + ".");
            } else if (belt != null && belt.getName().equalsIgnoreCase(itemName)) {
                player.removeBelt();
                player.addItem(belt);
                System.out.println("Successfully dequipped " + belt.getName() + ".");
            } else if (boots != null && boots.getName().equalsIgnoreCase(itemName)) {
                player.removeBoots();
                player.addItem(boots);
                System.out.println("Successfully dequipped " + boots.getName() + ".");
            } else if (gloves != null && gloves.getName().equalsIgnoreCase(itemName)) {
                player.removeGloves();
                player.addItem(gloves);
                System.out.println("Successfully dequipped " + gloves.getName() + ".");
            } else if (headBand != null && headBand.getName().equalsIgnoreCase(itemName)) {
                player.removeHeadBand();
                player.addItem(headBand);
                System.out.println("Successfully dequipped " + headBand.getName() + ".");
            } else if (ring != null && ring.getName().equalsIgnoreCase(itemName)) {
                player.removeRing();
                player.addItem(ring);
                System.out.println("Successfully dequipped " + ring.getName() + ".");
            } else {
                System.out.println("You are not wearing or wielding \"" + itemName + "\" currently.");
            }
        }
    },
    TALK {
        @Override
        public void execute(String[] args, GameState gameState) {
            boolean hasConveyance = false;
            String dialogueString = "";
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    hasConveyance = true;
                    dialogueString = ((Conveyance) npc).getDialogue();
                }
            }

            if (args.length < 1) {
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
                    String characterName = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
                    if (characterName.equalsIgnoreCase("conveyance") && hasConveyance) {
                        System.out.println(dialogueString);
                        System.out.println("The conversation is over.");
                        return;
                    }
                    Collection<NPC> people = gameState.getCurrentRoom().getPeopleInRoom().values();
                    boolean foundPerson = false;
                    for (NPC person : people) {
                        if (person.getName().equalsIgnoreCase(characterName)) {
                            try {
                                gameState.enterDialogue(person);
                                foundPerson = true;
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    if (!foundPerson) {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        System.out.println(characterName
                                + " was not found in this area. Use LOOK for a list of characters to talk to");
                    }
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
            if (args.length < 1) {
                System.out.println("Specify a character to join");
                return;
            }
    
            Map<String, NPC> characters = gameState.getCurrentRoom().getPeopleInRoom();
            String memberToJoin = args[0];
    
            boolean foundMember = false;
            for (Entry<String, NPC> character : characters.entrySet()) {
                if (character.getValue().getName().equalsIgnoreCase(memberToJoin)) {
                    foundMember = true;
                    if (character.getValue() instanceof PartyMember) {
                        boolean success = gameState.addPartyMember((PartyMember) character.getValue());
                        if (success) {
                            gameState.getCurrentRoom().removePersonFromRoom(character.getKey());
                            gameState.getCurrentRoom().updateMapEntry('.',
                                    Character.getNumericValue(character.getKey().charAt(1)),
                                    Character.getNumericValue(character.getKey().charAt(3)));
                            return;
                        }
                    } else if (character.getValue() instanceof Conveyance) {
                        gameState.addPartyMember((PartyMember) character.getValue());
                    }
                    else {
                        System.out.println("You aren't able to join this person. There are only a few valid party members available in the game, and their\n\t" +
                                "presence will be made clear (in the actual game, outside this demo. In the demo, the only valid character to join is Sam.)");
                    }
                }
            }
    
            if (!foundMember) {
                System.out.println("No character named " + memberToJoin + " was found in this room.");
            }
        }
    },  
    DISMISS {
        @SuppressWarnings("resource")
        @Override
        public void execute(String[] args, GameState gameState) {
            boolean hasConveyance = false;
            for (NPC npc : gameState.getCurrentParty()) {
                if (npc instanceof Conveyance) {
                    hasConveyance = true;
                }
            }

            if (hasConveyance) {
                System.out.println("You look at the conveyance. Over the time you have had it with you, you have been growing closer.");
                System.out.println("You have felt your mannerisms and its melding, and it is hard to imagine facing the world without it.");
                System.out.println("It seems to look back at you and seems to see you.");
                System.out.println("Somehow, although you are unsure whether this is synaptic or mechanical, you feel that it appreciates how it has helped you.");
                System.out.println("And it recognizes this place as unwelcome to it. He recognizes your will as distinct from -- maybe at odds with -- his own.");
                System.out.println("And it wants to leave.");
                System.out.println("You look at the conveyance. Do you dismiss it? (y/n).");
                Scanner scanner = new Scanner(System.in);
                String response = scanner.nextLine();
                while (!(response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n"))) {
                    System.out.println("Do you dismiss him? (y/n)");
                    response = scanner.nextLine();
                }
                if (response.equalsIgnoreCase("n")) {
                    System.out.println("This choice is comforting to you, and to some part of it. But it is bittersweet.");
                    System.out.println("As your gaze lingers, you see that on some level, it is glad to continue to help you.");
                    System.out.println("And on another, you know that he is acquiescing.");
                    return;
                }
                System.out.println("This was the right thing to do.");
                System.out.println("The path foreward will be harder, and there is not much you can do about that.");
                for (NPC npc : gameState.getCurrentParty()) {
                    if (npc instanceof Conveyance) {
                        gameState.removePartyMember(npc);
                        return;
                    }
                }
            } else {
                System.out.println("That command is not functional given your current party.");
            }
        }
    },
    ROB {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                System.out.println("Specify a character to rob");
                return;
            }
    
            NPC characterToRob = gameState.getCurrentRoom().getPeopleInRoom().get(args[0].toLowerCase());
            if (characterToRob == null) {
                System.out.println("Character not found.");
                return;
            }
    
            // Can't rob party members
            if (characterToRob instanceof PartyMember) {
                System.out.println("You cannot rob " + characterToRob.getName());
                return;
            }
    
            // Adjust alignment based on character type
            double alignmentDelta = gameState.getPlayer().getAlignment() * characterToRob.getAlignmentImpact() * 0.8;
            gameState.getPlayer().adjustAlignment(alignmentDelta);
    
            // Determine thievery success
            double random = Math.random() * 10;
            double playerScore = gameState.getPlayer().getWisdom() * 1.5 + gameState.getPlayer().getCharisma() + random;
            double npcScore = characterToRob.getWisdom() * 1.5 + characterToRob.getCharisma() + random;
    
            if (playerScore < npcScore) {
                handleFailedRobbery(characterToRob, gameState);
                return;
            }
    
            // Determine if character has anything to steal
            List<Item> characterInventory = characterToRob.getInventory();
            if (characterInventory.isEmpty()) {
                System.out.println(characterToRob.getName() + " does not have anything you can steal");
                return;
            }
    
            // Get highest value item to take
            Item itemToTake = characterInventory.stream()
                    .max(Comparator.comparingDouble(Item::getValue))
                    .orElseThrow(); // Optional: throw an exception if no items
    
            characterInventory.remove(itemToTake);
            characterToRob.setInventory(characterInventory);
    
            addItemToInventory(itemToTake, gameState, characterToRob);
            gameState.getPlayer().gainXP(20, gameState);
        }
    
        private void handleFailedRobbery(NPC characterToRob, GameState gameState) {
            System.out.println("Your attempt to rob " + characterToRob.getName() + " failed.");
            if (characterToRob instanceof Adversary || characterToRob instanceof Neutral) {
                gameState.getPlayer().gainXP(5, gameState);
                gameState.enterCombat(characterToRob);
            } else if (characterToRob instanceof Friend) {
                System.out.println("You feel that " + characterToRob.getName() + " will detect your presence if you continue, and you do not want to betray their trust");
            }
        }
    
        private void addItemToInventory(Item item, GameState gameState, NPC characterToRob) {
            if (gameState.getPlayer().getRemainingCarryWeight() > item.getWeight()) {
                gameState.getPlayer().addItem(item);
                gameState.getPlayer().reduceRemainingCarryWeight(item.getWeight());
                System.out.println("You successfully took " + item.getName() + " from " + characterToRob.getName());
                System.out.println(item.getName() + " was added to your inventory.");
            } else {
                gameState.safeZoneInventory.addItemToInventory(item);
                System.out.println("You successfully took " + item.getName() + " from " + characterToRob.getName());
                System.out.println(item.getName() + " was added to the safe zone inventory, because you do not have enough remaining carry weight.");
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
            // Print the current region name
            System.out.println("You are in the region \"" + gameState.getCurrentRegion().getRegionName() + "\"");
    
            // Check if there is a safe zone in the current region
            if (!gameState.getCurrentRegion().getHasSafeZone()) {
                System.out.println("There is not (yet) a safe zone in this region.");
                return;
            }
    
            Room currentRoom = gameState.getCurrentRoom();
            Region currentRegion = gameState.getCurrentRegion();
    
            // Check if the player is already in a safe room
            if (currentRoom.getIsSafe()) {
                System.out.println("You are already in the safe zone.");
                return;
            }
    
            // Construct the path to the safe zone
            StringBuilder pathToSafeZone = new StringBuilder("To get to the safe zone from your current position, go through: ");
            Room nextRoom = currentRoom;
    
            while (!nextRoom.getIsSafe()) {
                String nextRoomName = nextRoom.getNextRoomToSafeZone();
                if (nextRoomName == null) {
                    System.out.println("Cannot find a path to the safe zone from your current position.");
                    return;
                }
    
                pathToSafeZone.append("\"").append(nextRoomName).append("\"");
    
                // Find the next room
                nextRoom = currentRegion.getRooms().stream()
                    .filter(room -> room.getName().equalsIgnoreCase(nextRoomName))
                    .findFirst()
                    .orElse(null);
    
                if (nextRoom == null) {
                    System.out.println("Error: Path is incomplete or invalid.");
                    return;
                }
    
                // Append an arrow if the next room is not the last one in the path
                if (!nextRoom.getIsSafe()) {
                    pathToSafeZone.append(" -> ");
                }
            }
    
            // Print the complete path to the safe zone
            System.out.println(pathToSafeZone);
        }
    },    
    HIDE {
        @Override
        public void execute(String[] args, GameState gameState) {
            Random random = new Random();
            int chance = random.nextInt(10);
    
            // Determine if the hide attempt was successful
            if (chance == 0) {
                System.out.println("Hide attempt failed.");
            } else {
                System.out.println("Successfully hid.");
                gameState.getPlayer().setIsHiding(true);
            }
        }
    },    
    UNHIDE {
        @Override
        public void execute(String[] args, GameState gameState) {
            System.out.println("You are no longer hiding.");
            gameState.getPlayer().setIsHiding(false);
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