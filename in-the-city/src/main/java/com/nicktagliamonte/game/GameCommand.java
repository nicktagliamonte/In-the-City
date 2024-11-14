package com.nicktagliamonte.game;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.PartyMember;
import com.nicktagliamonte.characters.Person;
import com.nicktagliamonte.items.Item;

public enum GameCommand {
    LOOK {
        @Override
        public void execute(String[] args, GameState gameState) {
            // Get the current position from GameState
            String currentRoom = gameState.getcurrentRoom().getName();
            String roomDescription = gameState.getRoomDescription(currentRoom);
            Map<String, Item> visibleItems = gameState.getcurrentRoom().getItemsInRoom();
            Map<String, NPC> characters = gameState.getcurrentRoom().getPeopleInRoom();
            List<NPC> party = gameState.getCurrentParty();

            // Construct the dynamic message based on the game state
            StringBuilder message = new StringBuilder();
            message.append("You look around and see: \n").append(roomDescription).append("\n");

            if (!visibleItems.isEmpty()) {
                message.append("Items here: ");
                // Iterate over the entries of the visibleItems map
                for (Map.Entry<String, Item> entry : visibleItems.entrySet()) {
                    // For each item, append its name and key (coordinates)
                    message.append(entry.getValue().getName())
                           .append(" at ")
                           .append(entry.getKey());
                    
                    // Add a comma between items, but not after the last item
                    if (visibleItems.size() > 1 && !entry.equals(visibleItems.entrySet().toArray()[visibleItems.size() - 1])) {
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
                    
                    if (characters.size() > 1 && !entry.equals(characters.entrySet().toArray()[characters.size() - 1])) {
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
            gameState.getcurrentRoom().viewAdjascentRooms();
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
                    // Concatenate the rest of the arguments as the waypoint name
                    StringBuilder waypointNameBuilder = new StringBuilder();
                    for (int i = 1; i < arguments.length; i++) {
                        waypointNameBuilder.append(arguments[i]);
                        if (i < arguments.length - 1) {
                            waypointNameBuilder.append(" "); // Add space between words
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
                        System.out.println("Invalid number of steps. Please enter a valid integer, or just a direction to move 1 step in that direction.");
                        return;
                    }
                }
    
                String message = gameState.changeLocation(direction, distance);
                System.out.println(message);
            }
        }
    },
    ENTER {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                System.out.println("Which room do you want to enter?");
            } else {
                gameState.enterByCommand(args[0]);
            }
        }
    },
    ASCEND {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                System.out.println("Which room do you want to enter?");
            } else {
                gameState.ascend(args[0]);
            }
        }
    },
    DESCEND {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                System.out.println("Which room do you want to enter?");
            } else {
                gameState.descend(args[0]);
            }
        }
    },
    EXAMINE {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                System.out.println("You need to specify an item to examine");
                return;
            }
            Collection<Item> itemsInRoom = gameState.getcurrentRoom().getItemsInRoom().values();
            Item itemToExamine = null;
            for (Item item : itemsInRoom) {
                if (item.getName().equalsIgnoreCase(args[0])) {
                    itemToExamine = item;
                    break;
                }
            }
            if (itemToExamine != null) {
                // Print the description of the item.
                System.out.println(itemToExamine.getDescription());
            } else {
                // Handle the case where the item was not found in the room.
                System.out.println("There is no such item here: " + args[0]);
            }
        }
    },
    TAKE {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                System.out.println("You need to specify an item to take");
                return;
            }

            Map<String, Item> itemsInRoom = gameState.getcurrentRoom().getItemsInRoom();
            String itemLocation = null;
            Item itemToTake = null;
            for (Entry<String, Item> item : itemsInRoom.entrySet()) {
                if (item.getValue().getName().equalsIgnoreCase(args[0])) {
                    itemToTake = item.getValue();
                    itemLocation = item.getKey();
                    break;
                }
            }

            if (itemToTake != null) {
                if (gameState.getPlayer().getRemainingCarryWeight() > itemToTake.getWeight()) {
                    gameState.getPlayer().addItem(itemToTake);
                    gameState.getPlayer().reduceRemainingCarryWeight(itemToTake.getWeight());
                    gameState.getcurrentRoom().removeItemFromRoom(itemLocation);
                    System.out.printf("added %s to inventory\n", itemToTake.getName());
                } else {
                    System.out.println("That item is too heavy.");
                }                
            } else {
                System.out.println("There is no such item here: " + args[0]);
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

            List<Item> itemsInInventory = gameState.getPlayer().getInventory();

            if (itemsInInventory == null) {
                System.out.println("Inventory is empty, cannot drop item");
                return;
            }

            Item itemToDrop = null;
            for (Item item : itemsInInventory) {
                if (item.getName().equalsIgnoreCase(args[0])) {
                    itemToDrop = item;
                    break;
                }
            }

            if (itemToDrop == null) {
                System.out.printf("There is no %s in inventory", args[0]);
            } else {
                itemsInInventory.remove(itemToDrop);
                gameState.getPlayer().setInventory(itemsInInventory);
                gameState.getPlayer().increaseRemainingCarryWeight(itemToDrop.getWeight());
                gameState.getcurrentRoom().addItemToRoom(gameState.getcurrentRoom().getPlayerPosition(), itemToDrop);
                System.out.println("Dropped " + itemToDrop.getName());
            }            
        }
    },
    INVENTORY {
        @Override
        public void execute(String[] args, GameState gameState) {
            gameState.getPlayer().listInventory();
            
        }
    },
    USE {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length == 0) {
                System.out.println("Specify an item to use.");
                return;
            }
    
            String itemName = args[0].toLowerCase(); // Get the item name from the args
            Item item = gameState.getPlayer().getItemFromInventory(itemName);
    
            if (item != null) {
                item.use(gameState); // Call the item's specific use method
            } else {
                System.out.println("You don't have that item.");
            }
        }
    },
    TALK {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length == 0) {
                System.out.println("Specify who to talk to");
                return;
            }

            Collection<NPC> characters = gameState.getcurrentRoom().getPeopleInRoom().values();
            List<NPC> party = gameState.getCurrentParty();
            String chosenCharacter = args[0];

            for (NPC character : characters) {
                if (character.getName().equalsIgnoreCase(chosenCharacter)) {
                    System.out.println(character.getRandomDialogue());
                    return;
                    //TODO: replace the above with something like gameState.enterDialogue(character);
                }
            }
            for (NPC partyMember : party) {
                if (partyMember.getName().equalsIgnoreCase(chosenCharacter)) {
                    System.out.println(partyMember.getRandomDialogue());
                    return;
                    //TODO: replace the above with something like gameState.enterDialogue(character);
                }
            }
            System.out.println("I don't recognize that name.  use LOOK to get a list of NPCs in the current room by name");
        }
    },
    JOIN {
        @Override
        public void execute(String[] args, GameState gameState) {
            Map<String, NPC> characters = gameState.getcurrentRoom().getPeopleInRoom();
            String memberToJoin = args[0];

            for (Entry<String, NPC> character : characters.entrySet()) {
                if (character.getValue().getName().equalsIgnoreCase(memberToJoin)) {
                    if (character.getValue() instanceof PartyMember) {
                        boolean success = gameState.addPartyMember((PartyMember) character.getValue());
                        if (success) {
                            gameState.getcurrentRoom().removePersonFromRoom(character.getKey());
                            return;
                        }
                    }
                }
            }
        }
    },
    MENU {
        @Override
        public void execute(String[] args, GameState gameState) {
            GameEngine engine = gameState.getGameEngine();
            engine.launchMenu();
        }
    },
    LOCATE {
        @Override
        public void execute(String[] args, GameState gameState) {
            // TODO: this will need to be a different format to indicate the overall game region and give more workable information, 
            // once i'm out of this two room test environment
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

            Collection<NPC> characters = gameState.getcurrentRoom().getPeopleInRoom().values();
            String chosenCharacter = args[0];

            for (Person character : characters) {
                if (character.getName().equalsIgnoreCase(chosenCharacter)) {
                    gameState.enterCombat(character);
                }
            }
            System.out.println("I don't recognize that name");
        }
    },
    HINT {
        //TODO: make sure this method reduces the players hints, and or checks on how many are remaining.  
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length == 0) {
                System.out.println("Specify who to get a hint from");
                return;
            }

            Collection<NPC> characters = gameState.getcurrentRoom().getPeopleInRoom().values();
            String chosenCharacter = args[0];

            for (NPC character : characters) {
                if (character.getName().equalsIgnoreCase(chosenCharacter)) {
                    System.out.println(character.getHint());
                    return;
                    //TODO: replace the above with something like gameState.enterDialogue(character);
                }
            }
            System.out.println("I don't recognize that name.  use LOOK to get a list of NPCs in the current room by name");
        }
    },
    SAVE {
        @Override
        public void execute(String[] args, GameState gameState) {
            gameState.getGameEngine().saveGame();
        }
    },
    LOAD {
        @Override
        public void execute(String[] args, GameState gameState) {
            gameState.getGameEngine().loadGame();
        }
    },    
    QUIT {
        @Override
        public void execute(String[] args, GameState gameState) {
            //quit logic handled in game engine
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