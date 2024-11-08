import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {
    private String currentLocation;
    private Map<String, String> roomDescriptions;
    private Map<String, List<Item>> itemsInRooms;
    private Map<String, List<Person>> charactersInRooms;
    public Player player;
    public GameEngine gameEngine;

    // Constructor
    public GameState(Player player, GameEngine gameEngine) {
        // Initialize the game state, including descriptions and room contents
        roomDescriptions = new HashMap<>();
        itemsInRooms = new HashMap<>();
        charactersInRooms = new HashMap<>();
        this.player = player;
        this.gameEngine = gameEngine;

        currentLocation = "Starting Room";
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public String getRoomDescription(String location) {
        return roomDescriptions.getOrDefault(location, "An unfamiliar place.");
    }

    public List<Item> getItemsInCurrentRoom() {
        return itemsInRooms.getOrDefault(currentLocation, new ArrayList<>());
    }

    public List<Person> getCharactersInCurrentRoom() {
        return charactersInRooms.getOrDefault(currentLocation, new ArrayList<>());
    }

    public String changeLocation(String direction) {
        StringBuilder sb = new StringBuilder("You move ");
        sb.append(direction);
        return sb.toString();
    }

    public Player getPlayer() {
        return player;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public String getDirectionsToRegion() {
        //this is i think going to be a pathfinding operation.  this is going to be massively difficult to implement
        String ret = "working";
        return ret;
    }

    public String getDirectionsToEconomicZone() {
        //this is i think going to be a pathfinding operation.  this is going to be massively difficult to implement
        String ret = "working";
        return ret;
    }

    public String toSerializableFormat() {
        //this one is going to also be somewhat of an undertaking
        return "working";
    }

    public void fromSerializableFormat(String data) {
        // Parse the data and populate the game state
        // Example: Deserialize JSON or parse plain text to restore fields
        //this is a minor nightmare when it comes time.
    }

    public void enterDialogue(Person character) {
        //I'm not wholly clear on what this will look like
    }

    public void enterCombat(Person character) {
        //not wholly clear on this either
    }
}