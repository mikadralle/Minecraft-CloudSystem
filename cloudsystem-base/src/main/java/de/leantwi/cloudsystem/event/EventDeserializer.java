package de.leantwi.cloudsystem.event;

import com.google.gson.*;
import de.leantwi.cloudsystem.api.event.Event;

import java.lang.reflect.Type;

public class EventDeserializer implements
        JsonSerializer<Event>, JsonDeserializer<Event> {

    public JsonElement serialize(Event src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));

        return result;
    }

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        System.out.println("TYPE IS: " + type);

        try {
            return context.deserialize(element, Class.forName("de.leantwi.cloudsystem.api.event." + type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }


}
