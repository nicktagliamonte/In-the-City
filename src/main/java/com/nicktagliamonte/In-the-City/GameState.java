import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {
    private String currentLocation;
    private Map<String, String> roomDescriptions;
    private Map<String, List<Item>> itemsInRooms;
    private Map<String, List<Person>> charactersInRooms;

    // Constructor
    public GameState() {
        // Initialize the game state, including descriptions and room contents
        roomDescriptions = new HashMap<>();
        itemsInRooms = new HashMap<>();
        charactersInRooms = new HashMap<>();

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
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder("You move ");
        sb.append(direction);
        return sb.toString();
    }

    // Additional methods to change location, add/remove items, etc.
}