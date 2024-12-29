package com.nicktagliamonte.items;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktagliamonte.characters.NPC;
import com.nicktagliamonte.characters.PartyMember;
import com.nicktagliamonte.game.GameState;

public class Scrap extends Item{
    public Scrap() {
        super("Scrap material", "Random, salvaged parts from machines (e.g., screws, metal shards, old wires, wood).", 1.0, false, 1, "", true);
    }

    @Override
    public void use(GameState gameState) {
        boolean hasTechnologist;
        List<String> partyClasses = new ArrayList<>();
        for (NPC member : gameState.getCurrentParty()) {
            PartyMember pm = (PartyMember) member;
            partyClasses.add(pm.getCharacterClass().getClassName());
        }

        hasTechnologist = (gameState.getPlayer().getCharacterClass().getClassName().equalsIgnoreCase("technologist") || 
        partyClasses.contains("technologist"));

        if (hasTechnologist) {
            System.out.println("You craft the item into a small magical trap");
            gameState.getPlayer().gainXP(4, gameState);
            Trap trap = new SmallMagicalTrap(gameState);
            gameState.getPlayer().craftItem(trap);
        } else {
            System.out.println("You craft the item into a small trap");
            gameState.getPlayer().gainXP(2, gameState);
            Trap trap = new SmallTrap(gameState);
            gameState.getPlayer().craftItem(trap);
        }        
    }

    public String toSerializableFormat() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String baseJson = gson.toJson(this);
        JsonObject jsonObject = JsonParser.parseString(baseJson).getAsJsonObject();
        jsonObject.addProperty("type", "Scrap");
        jsonObject.addProperty("quantity", 1);
        return gson.toJson(jsonObject);
    }
}
