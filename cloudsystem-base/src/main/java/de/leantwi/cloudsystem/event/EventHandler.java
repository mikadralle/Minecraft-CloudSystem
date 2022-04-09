package de.leantwi.cloudsystem.event;

import com.google.gson.*;
import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.event.Event;
import de.leantwi.cloudsystem.api.event.EventHandlerAPI;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.event.dispatcher.EventDispatcher;
import io.nats.client.Connection;

import java.lang.reflect.Type;


public class EventHandler implements EventHandlerAPI {

    public final EventBus eventBus;
    private final GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Event.class, new PacketSerializer<Event>());

    public EventHandler(Connection connection) {
        this.eventBus = new EventBus();
        EventDispatcher eventDispatcher = new EventDispatcher(connection, this);
        eventDispatcher.listen();

    }

    public Event getEventByJsonKey(String jsonKey) {
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(jsonKey, Event.class);
    }
    public String getJsonKeyFromEvent(Event event){
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(event,Event.class);
    }

    @Override
    public void registerListener(Listener listener) {
        this.eventBus.register(listener);

    }

    @Override
    public void callEvent(Event event) {
        CloudSystem.getAPI().getNatsConnector().publish("events", getJsonKeyFromEvent(event));
    }

    @Override
    public void postEvent(Event event) {
        this.eventBus.post(event);

    }

    static class PacketSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {

        private static final String TYPE = "type";

        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String className = jsonObject.get(TYPE).getAsString();
            try {
                return context.deserialize(json, Class.forName(className));
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            JsonElement jsonElement = context.serialize(src, src.getClass());
            jsonElement.getAsJsonObject().addProperty(TYPE, src.getClass().getCanonicalName());
            return jsonElement;
        }
    }

}
