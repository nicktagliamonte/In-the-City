package com.nicktagliamonte.game;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.nicktagliamonte.Spells.Spell;
import com.nicktagliamonte.Spells.SpellDeserializer;
import com.nicktagliamonte.characters.CharacterClass;
import com.nicktagliamonte.characters.CharacterDeserializer;
import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.Player;
import com.nicktagliamonte.items.Amulet;
import com.nicktagliamonte.items.Armor;
import com.nicktagliamonte.items.Belt;
import com.nicktagliamonte.items.Boots;
import com.nicktagliamonte.items.Gloves;
import com.nicktagliamonte.items.HeadBand;
import com.nicktagliamonte.items.Item;
import com.nicktagliamonte.items.ItemDeserializer;
import com.nicktagliamonte.items.Ring;
import com.nicktagliamonte.items.Weapon;
import com.nicktagliamonte.quests.Quest;
import com.nicktagliamonte.rooms.Adjacency;
import com.nicktagliamonte.rooms.AdjacencyDeserializer;
import com.nicktagliamonte.rooms.Room;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

public class Load {  
    private final ItemDeserializer itemDeserializer = new ItemDeserializer();
    private final SpellDeserializer spellDeserializer = new SpellDeserializer();
    private final AdjacencyDeserializer adjacencyDeserializer = new AdjacencyDeserializer();
    private final CharacterDeserializer characterDeserializer = new CharacterDeserializer();
    @Expose public String filePath;
    @Expose public JsonObject json;
    @Expose public JsonObject gameStateObject;
    @Expose public int turnsInStealth;
    @Expose public boolean inDialogue;
    @Expose public boolean fromSaveFile = true;
    @Expose public Player player;
    @Expose public Region region;
    @Expose public Room currentRoom;
    @Expose public safeZoneInventory safeZoneInventory = new safeZoneInventory();
    @Expose public List<NPC> currentParty = new ArrayList<>();

    public Load(String filePath) {
        this.filePath = filePath;
        parseFile();
        getGameEngineData();
        initializePlayer();
        getGameStateData();
        initializeRegion();
    }

    public void parseFile() {
        try (Reader reader = new FileReader(filePath)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            json = jsonObject;
            gameStateObject = json.get("gameState").getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getGameEngineData() {
        turnsInStealth = json.get("turnsInStealth").getAsInt();
    }

    public void initializePlayer() {
        JsonObject playerObject = gameStateObject.get("player").getAsJsonObject();
        JsonObject characterClassObject = playerObject.get("characterClass").getAsJsonObject();
        CharacterClass characterClass = CharacterClass.createCharacterClass(characterClassObject.get("className").getAsString());
        player = new Player(playerObject.get("name").getAsString(), characterClass);

        // Extract and set all simple data types
        player.setStrength(playerObject.get("strength").getAsDouble());
        player.setDexterity(playerObject.get("dexterity").getAsDouble());
        player.setConstitution(playerObject.get("constitution").getAsDouble());
        player.setIntelligence(playerObject.get("intelligence").getAsDouble());
        player.setWisdom(playerObject.get("wisdom").getAsDouble());
        player.setCharisma(playerObject.get("charisma").getAsDouble());
        player.setMaxCarryWeight(playerObject.get("maxCarryWeight").getAsDouble());
        player.setRemainingCarryWeight(playerObject.get("remainingCarryWeight").getAsDouble());
        player.setAc(playerObject.get("ac").getAsDouble());
        player.setMaxHealth(playerObject.get("maxHealth").getAsDouble());
        player.setStatus(playerObject.get("status").getAsString());
        player.setTimeSinceFood(playerObject.get("timeSinceFood").getAsInt());
        player.setTimeSinceWater(playerObject.get("timeSinceWater").getAsInt());
        player.setAlignment(playerObject.get("alignment").getAsDouble());
        player.setIsHiding(playerObject.get("isHiding").getAsBoolean());
        player.setLevel(playerObject.get("level").getAsInt());
        player.setCurrentXP(playerObject.get("currentXP").getAsDouble());
        player.setNextLevelXP(playerObject.get("nextLevelXP").getAsDouble());
        player.setNextLevelXPReward(playerObject.get("nextLevelXPReward").getAsDouble());
        player.setHealth(playerObject.get("health").getAsDouble());
        player.setInitiative(playerObject.get("initiative").getAsInt());

        // Create a Gson instance with the ItemDeserializer
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Item.class, itemDeserializer)
            .registerTypeAdapter(Spell.class, spellDeserializer)
            .create();

        // Deserialize equipment fields conditionally
        if (playerObject.has("armor")) {
            player.setArmor(((Armor) gson.fromJson(playerObject.get("armor"), Item.class)));
        }
        if (playerObject.has("weapon")) {
            player.setWeapon(((Weapon) gson.fromJson(playerObject.get("weapon"), Item.class)));
        }
        if (playerObject.has("headBand")) {
            player.setHeadBand(((HeadBand) gson.fromJson(playerObject.get("headBand"), Item.class)));
        }
        if (playerObject.has("ring")) {
            player.setRing(((Ring) gson.fromJson(playerObject.get("ring"), Item.class)));
        }
        if (playerObject.has("amulet")) {
            player.setAmulet(((Amulet) gson.fromJson(playerObject.get("amulet"), Item.class)));
        }
        if (playerObject.has("boots")) {
            player.setBoots(((Boots) gson.fromJson(playerObject.get("boots"), Item.class)));
        }
        if (playerObject.has("gloves")) {
            player.setGloves(((Gloves) gson.fromJson(playerObject.get("gloves"), Item.class)));
        }
        if (playerObject.has("belt")) {
            player.setBelt(((Belt) gson.fromJson(playerObject.get("belt"), Item.class)));
        }

        // Deserialize inventory
        if (playerObject.has("inventory")) {
            JsonArray inventoryArray = playerObject.getAsJsonArray("inventory");
            List<Item> inventory = new ArrayList<>();
            for (JsonElement element : inventoryArray) {
                inventory.add(gson.fromJson(element, Item.class));
            }
            player.setInventory(inventory);
        }

        // Deserialize spellbook
        if (playerObject.has("spellbook")) {
            JsonArray spellArray = playerObject.getAsJsonArray("spellbook");
            List<Spell> spells = new ArrayList<>();
            for (JsonElement element : spellArray) {
                spells.add(gson.fromJson(element, Spell.class));
            }
            player.setSpellbook(spells);
        }

        // Deserialize questlog
        if (playerObject.has("questLog")) {
            JsonArray questArray = playerObject.getAsJsonArray("questLog");
            List<Quest> quests = new ArrayList<>();
            for (JsonElement element : questArray) {
                quests.add(gson.fromJson(element, Quest.class));
            }
            player.setQuestLog(quests);
        }
    }

    public void getGameStateData() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Item.class, itemDeserializer)
                .registerTypeAdapter(NPC.class, characterDeserializer)
                .create();
        inDialogue = gameStateObject.get("inDialogue").getAsBoolean();
        JsonObject safeZoneObject = gameStateObject.get("safeZoneInventory").getAsJsonObject();
        JsonArray safeZoneArray = safeZoneObject.get("safeZoneInventory").getAsJsonArray();
        for (JsonElement element : safeZoneArray) {
            List<Item> items = gson.fromJson(element, new TypeToken<List<Item>>() {}.getType());
            for (Item item : items) {
                safeZoneInventory.addItemToInventory(item);
            }
        }

        JsonObject partyObject = gameStateObject.get("currentParty").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : partyObject.entrySet()) {
            JsonObject npcData = entry.getValue().getAsJsonObject();

            NPC npc = gson.fromJson(npcData, NPC.class);
            currentParty.add(npc);
        }
    }

    public void initializeRegion() {
        JsonObject regionObject = gameStateObject.get("currentRegion").getAsJsonObject();

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .excludeFieldsWithoutExposeAnnotation()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .registerTypeAdapter(Adjacency.class, adjacencyDeserializer)
                .registerTypeAdapter(Item.class, itemDeserializer)
                .registerTypeAdapter(NPC.class, characterDeserializer)
                .create();

        // Deserialize region (and rooms)
        region = gson.fromJson(regionObject, Region.class);
        region.setHasSafeZone(regionObject.get("hasSafeZone").getAsBoolean());

        // Manually handle adjacencies
        JsonArray roomsArray = regionObject.getAsJsonArray("rooms");
        List<Room> rooms = region.getRooms();

        if (roomsArray.size() != rooms.size()) {
            throw new IllegalStateException("Mismatch between number of rooms in the REGION and number of rooms in the JSON. switch to manually deserializing rooms, check Load.java method \"initializeRegion\"");
        }

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            JsonObject roomJson = roomsArray.get(i).getAsJsonObject();
    
            // Deserialize adjacencies for this room
            if (roomJson.has("adjacentRooms")) {
                JsonArray adjacenciesArray = roomJson.getAsJsonArray("adjacentRooms");
                List<Adjacency> adjacencies = new ArrayList<>();
    
                for (JsonElement adjacencyElement : adjacenciesArray) {
                    Adjacency adjacency = gson.fromJson(adjacencyElement, Adjacency.class);
                    adjacencies.add(adjacency);
                }
    
                // Set the adjacencies list in the room object
                room.setAdjacentRooms(adjacencies);
            }
        }

        // Manually handle items in room
        deserializeItems(regionObject, gson);

        // Manually handle people in room
        deserializePeople(regionObject, gson);

        // Manually handle current room
        setCurrentRoom(regionObject);
    }

    public void deserializeItems(JsonObject regionObject, Gson gson) {
        JsonArray roomsArray = regionObject.getAsJsonArray("rooms");
        List<Room> rooms = region.getRooms();
    
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            JsonObject roomJson = roomsArray.get(i).getAsJsonObject();
    
            if (roomJson.has("itemsInRoom")) {
                JsonObject itemsInRoomJson = roomJson.getAsJsonObject("itemsInRoom");
                Map<String, List<Item>> itemsInRoom = new HashMap<>();
    
                for (Map.Entry<String, JsonElement> entry : itemsInRoomJson.entrySet()) {
                    String coordinates = entry.getKey();
                    JsonArray itemsArray = entry.getValue().getAsJsonArray();
    
                    // Use ItemDeserializer to deserialize the list of items
                    List<Item> items = gson.fromJson(itemsArray, new TypeToken<List<Item>>() {}.getType());
    
                    // Put the list of items for the current coordinate
                    itemsInRoom.put(coordinates, items);
                }
    
                // Flatten the lists of items for each coordinate
                Map<String, List<Item>> flattenedItemsInRoom = new HashMap<>();
                for (Map.Entry<String, List<Item>> entry : itemsInRoom.entrySet()) {
                    List<Item> flattenedItems = new ArrayList<>();
    
                    // Check if the list contains nested lists and flatten them
                    for (Object itemOrList : entry.getValue()) {
                        if (itemOrList instanceof Iterable<?>) {
                            for (Object nestedItem : (Iterable<?>) itemOrList) {
                                flattenedItems.add((Item) nestedItem);
                            }
                        } else {
                            flattenedItems.add((Item) itemOrList);
                        }
                    }
    
                    // Put the flattened list in the new map
                    flattenedItemsInRoom.put(entry.getKey(), flattenedItems);
                }
    
                // Set the flattened items in the room
                room.setItemsInRoom(flattenedItemsInRoom);
            } else {
                // Initialize an empty map if no itemsInRoom field exists
                room.setItemsInRoom(new HashMap<>());
            }
        }
    }    

    public void deserializePeople(JsonObject regionObject, Gson gson) {
        JsonArray roomsArray = regionObject.getAsJsonArray("rooms");
        List<Room> rooms = region.getRooms();
    
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            JsonObject roomJson = roomsArray.get(i).getAsJsonObject();
    
            if (roomJson.has("peopleInRoom")) {
                JsonObject peopleInRoomJson = roomJson.getAsJsonObject("peopleInRoom");
                Map<String, NPC> peopleInRoom = new HashMap<>();
    
                for (Map.Entry<String, JsonElement> entry : peopleInRoomJson.entrySet()) {
                    String coordinates = entry.getKey();
                    JsonObject npcJson = entry.getValue().getAsJsonObject();
    
                    // Use CharacterDeserializer to deserialize the NPC
                    NPC npc = gson.fromJson(npcJson, NPC.class);
                    peopleInRoom.put(coordinates, npc);
                }
    
                // Set the people in the room
                room.setPeopleInRoom(peopleInRoom);
            } else {
                // Initialize an empty map if no peopleInRoom field exists
                room.setPeopleInRoom(new HashMap<>());
            }
        }
    }
    
    public void setCurrentRoom(JsonObject regionObject) {
        String currentRoomName = gameStateObject.get("currentRoom").getAsString();
        List<Room> rooms = region.getRooms();
    
        for (Room room : rooms) {
            if (room.getName().equalsIgnoreCase(currentRoomName)) {
                currentRoom = room;
                return;
            }
        }
    
        // If no room matches, throw an exception (this should never happen based on the guarantee)
        throw new IllegalStateException("No room found with name matching currentRoom: " + currentRoomName);
    }
}