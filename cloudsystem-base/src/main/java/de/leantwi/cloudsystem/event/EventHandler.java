package de.leantwi.cloudsystem.event;

import com.google.gson.Gson;
import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.event.Event;
import de.leantwi.cloudsystem.api.event.EventHandlerAPI;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.event.dispatcher.EventDispatcher;
import io.nats.client.Connection;


public class EventHandler implements EventHandlerAPI {

    private final EventBus eventBus;
    private final Gson gson = new Gson();
    public EventHandler(Connection connection) {
        this.eventBus = new EventBus();
        EventDispatcher eventDispatcher = new EventDispatcher(connection,this);
        eventDispatcher.listen();

    }

    @Override
    public void registerListener(Listener listener) {
        this.eventBus.register(listener);

    }

    @Override
    public void callEvent(Event event) {
        CloudSystem.getAPI().getNatsConnector().publish("events",gson.toJson(event));
    }

    @Override
    public void postEvent(Event event) {
        System.out.println("psot event: " + event.getClass().getName());
        this.eventBus.post(event);

    }

}
