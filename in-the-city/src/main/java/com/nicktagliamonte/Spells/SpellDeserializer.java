package com.nicktagliamonte.Spells;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class SpellDeserializer implements JsonDeserializer<Spell> {
    @Override
    public Spell deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (!jsonObject.has("type")) {
            throw new JsonParseException("Spell type is missing from the JSON");
        }

        String spellType = jsonObject.get("type").getAsString();

        switch (spellType) {
            case "IonSurge":
                return new IonSurge();
            case "ArcaneConduit":
                return new ArcaneConduit();
            case "CryoBurst":
                return new CryoBurst();
            case "EmpPulse":
                return new EmpPulse();
            case "GravityWell":
                return new GravityWell();
            case "NanoSwarm":
                return new NanoSwarm();
            case "ParticleStream":
                return new ParticleStream();
            case "PlasmaBolt":
                return new PlasmaBolt();
            case "ThermalLance":
                return new ThermalLance();
            default:
                throw new JsonParseException("Unknown Spell Type: " + spellType);
        }
    }
        
}
