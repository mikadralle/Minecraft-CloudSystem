package de.leantwi.cloudsystem.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.event.Event;
import de.leantwi.cloudsystem.api.event.EventHandlerAPI;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.event.dispatcher.EventDispatcher;
import io.nats.client.Connection;


public class EventHandler implements EventHandlerAPI {

    public final EventBus eventBus;
    public final Gson gson;
    public EventHandler(Connection connection) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Event.class,new EventDeserializer());
        this.gson = gsonBuilder.create();
        this.eventBus = new EventBus();
        EventDispatcher eventDispatcher = new EventDispatcher(connection,this,eventBus);
        eventDispatcher.listen();

    }

    @Override
    public void registerListener(Listener listener) {
        this.eventBus.register(listener);

    }

    @Override
    public void callEvent(Event event) {
        CloudSystem.getAPI().getNatsConnector().publish("events",writeBuff(event));
    }

    @Override
    public void postEvent(Event event) {
        System.out.println("psot event: " + event.getClass().getName());
        this.eventBus.post(event);

    }

    @Override
    public Event readBuff(String json) {
    return gson.fromJson(json,Event.class);
    }

    @Override
    public String writeBuff(Event event) {
    return gson.toJson(event);
    }

}
