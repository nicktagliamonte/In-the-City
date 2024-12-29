package com.nicktagliamonte.characters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.game.GameState;
import com.nicktagliamonte.items.Item;

public class Conveyance extends PartyMember {
    private transient GameState gameState;
    private List<String> dialogue;
    private boolean inHunger;
    public int timeSinceFuel;

    public Conveyance(GameState gameState) {
        super("Conveyance", new ArrayList<>(), "A large conveyance, with space for the party and many items. Through some strange link, you can access all of your safe zone inventory through here. When operating the conveyance, it is unclear whether you are driving or riding it.", 0, new CharacterClass("conveyance"), false);
    
        this.gameState = gameState;
        super.setInventory(gameState.safeZoneInventory.getInventory());

        dialogue = new ArrayList<>();
        dialogue.add("The conveyance hums softly, a sound that could be contentment.");
        dialogue.add("A low groan escapes the conveyance, reluctant but resolute.");
        dialogue.add("The conveyance vibrates briefly, as if stretching its limits.");
        dialogue.add("A faint whir or sigh emanates from the conveyance, tired but enduring.");
        dialogue.add("The conveyance emits a deep, resonant thrum, a sound of steady determination.");
        dialogue.add("With a shudder, the conveyance seems to protest but presses onward.");
        dialogue.add("The conveyance lets out a brief, rhythmic clatter that sounds almost playful.");
        dialogue.add("A long, low creak comes from the conveyance, like a weary complaint.");
        dialogue.add("The conveyance rattles quietly, a noise that could be amusement or frustration.");
        dialogue.add("A quiet hiss escapes the conveyance, a fleeting moment of exasperation.");
        dialogue.add("The conveyance emits a series of soft, rhythmic clicks, as if in approval.");
        dialogue.add("A deep, resonant hum rises from the conveyance, carrying a sense of pride.");
        dialogue.add("The conveyance groans faintly, a sound that speaks of old weariness.");
        dialogue.add("A sharp, metallic scrape is followed by a gentle whine, both fleeting.");
        dialogue.add("The conveyance lets out a low, vibrating pulse that almost feels like a laugh.");

        this.inHunger = false;
        this.timeSinceFuel = 0;
    }

    public Conveyance() {
        super("Conveyance", new ArrayList<>(), "A large conveyance, with space for the party and many items. Through some strange link, you can access all of your safe zone inventory through here. When operating the conveyance, it is unclear whether you are driving or riding it.", 0, new CharacterClass("conveyance"), false);
    
        dialogue = new ArrayList<>();
        dialogue.add("The conveyance hums softly, a sound that could be contentment.");
        dialogue.add("A low groan escapes the conveyance, reluctant but resolute.");
        dialogue.add("The conveyance vibrates briefly, as if stretching its limits.");
        dialogue.add("A faint whir or sigh emanates from the conveyance, tired but enduring.");
        dialogue.add("The conveyance emits a deep, resonant thrum, a sound of steady determination.");
        dialogue.add("With a shudder, the conveyance seems to protest but presses onward.");
        dialogue.add("The conveyance lets out a brief, rhythmic clatter that sounds almost playful.");
        dialogue.add("A long, low creak comes from the conveyance, like a weary complaint.");
        dialogue.add("The conveyance rattles quietly, a noise that could be amusement or frustration.");
        dialogue.add("A quiet hiss escapes the conveyance, a fleeting moment of exasperation.");
        dialogue.add("The conveyance emits a series of soft, rhythmic clicks, as if in approval.");
        dialogue.add("A deep, resonant hum rises from the conveyance, carrying a sense of pride.");
        dialogue.add("The conveyance groans faintly, a sound that speaks of old weariness.");
        dialogue.add("A sharp, metallic scrape is followed by a gentle whine, both fleeting.");
        dialogue.add("The conveyance lets out a low, vibrating pulse that almost feels like a laugh.");

        this.inHunger = false;
        this.timeSinceFuel = 0;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        super.setInventory(gameState.safeZoneInventory.getInventory());
    }

    public void setDialogue(List<String> dialogue) {
        this.dialogue = dialogue;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getDialogue() {
        Random random = new Random();
        int index = random.nextInt(dialogue.size());
        return dialogue.get(index);
    }

    public boolean getInHunger() {
        return inHunger;
    }

    public void setInHunger(boolean inHunger) {
        this.inHunger = inHunger;
    }

    public int getTimeSinceFuel() {
        return timeSinceFuel;
    }

    public void setTimeSinceFuel(int timeSinceFuel) {
        this.timeSinceFuel = timeSinceFuel;
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();

        // Adding the type of the character class
        jsonObject.addProperty("type", "Conveyance");

        // Serializing the inventory items (from the parent PartyMember)
        JsonArray serializedInventory = new JsonArray();
        for (Item item : super.getInventory()) {
            serializedInventory.add(JsonParser.parseString(item.toSerializableFormat()));
        }
        jsonObject.add("inventory", serializedInventory);

        // Adding dialogue (as a list of strings)
        JsonArray serializedDialogue = new JsonArray();
        for (String dialogueLine : this.dialogue) {
            serializedDialogue.add(dialogueLine);
        }
        jsonObject.add("dialogue", serializedDialogue);

        // Adding custom properties for Conveyance
        jsonObject.addProperty("inHunger", this.inHunger);
        jsonObject.addProperty("timeSinceFuel", this.timeSinceFuel);

        // Returning the JSON representation
        return gson.toJson(jsonObject);
    }
}