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
    
    QUIT {
        @Override
        public void execute(String[] args, GameState gameState) {
            //quit logic handled in game engine
        }
    }, 
    // Other commands...
    ;

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