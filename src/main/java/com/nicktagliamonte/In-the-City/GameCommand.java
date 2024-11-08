import java.util.List;

public enum GameCommand {
    LOOK {
        @Override
        public void execute(String[] args, GameState gameState) {
            // Get the current position from GameState
            String currentLocation = gameState.getCurrentLocation();
            String roomDescription = gameState.getRoomDescription(currentLocation);
            List<Item> visibleItems = gameState.getItemsInCurrentRoom();
            List<Person> characters = gameState.getCharactersInCurrentRoom();

            // Construct the dynamic message based on the game state
            StringBuilder message = new StringBuilder();
            message.append("You look around and see: \n").append(roomDescription).append("\n");

            if (!visibleItems.isEmpty()) {
                message.append("Items here: ").append(String.join(", ", visibleItems.toString())).append("\n");
            } else {
                message.append("There are no notable items here.\n");
            }

            if (!characters.isEmpty()) {
                message.append("Characters present: ").append(String.join(", ", characters.toString())).append("\n");
            } else {
                message.append("There is no one else here.\n");
            }

            // Print the message
            System.out.println(message.toString());
        }
    },
    MOVE {
        @Override
        public void execute(String[] args, GameState gameState) {
            String message = "";
            
            if (args.length < 1) {
                System.out.println("Which direction do you want to go?");
            } else {
                message = gameState.changeLocation(args[0]);
            }
            System.out.println(message);
        }
    },
    EXAMINE {
        @Override
        public void execute(String[] args, GameState gameState) {
            if (args.length < 1) {
                System.out.println("You need to specify an item to examine");
                return;
            }
            List<Item> itemsInRoom = gameState.getItemsInCurrentRoom();
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

            List<Item> itemsInRoom = gameState.getItemsInCurrentRoom();
            Item itemToTake = null;
            for (Item item : itemsInRoom) {
                if (item.getName().equalsIgnoreCase(args[0])) {
                    itemToTake = item;
                    break;
                }
            }

            if (itemToTake != null) {
                if (gameState.getPlayer().getMaxCarryWeight() < itemToTake.getWeight()) {
                    gameState.getPlayer().addItem(itemToTake);
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

            List<Person> characters = gameState.getCharactersInCurrentRoom();
            String chosenCharacter = args[0];

            for (Person character : characters) {
                if (character.getName().equalsIgnoreCase(chosenCharacter)) {
                    gameState.enterDialogue(character);
                }
            }
            System.out.println("I don't recognize that name");
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
            String region = gameState.getCurrentLocation();
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

            List<Person> characters = gameState.getCharactersInCurrentRoom();
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
        @Override
        public void execute(String[] args, GameState gameState) {
            gameState.getPlayer().useHint();
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