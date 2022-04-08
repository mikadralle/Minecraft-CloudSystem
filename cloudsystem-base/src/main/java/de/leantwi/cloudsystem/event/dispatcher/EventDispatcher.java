package de.leantwi.cloudsystem.event.dispatcher;

import com.google.gson.Gson;
import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.event.Event;
import de.leantwi.cloudsystem.event.EventHandler;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;

import java.nio.charset.StandardCharsets;


public class EventDispatcher {

    private final Gson gson = new Gson();
    private Connection connection;
    private EventHandler eventHandler;
    public EventDispatcher(Connection connection, EventHandler eventHandler) {
        this.connection = connection;
        this.eventHandler = eventHandler;

    }

    public void listen() {

        final Dispatcher eventDispatcher = connection.createDispatcher(message -> {

            final String msg = new String(message.getData(), StandardCharsets.UTF_8);
            eventHandler.postEvent(gson.fromJson(msg,Event.class));
        });


        eventDispatcher.subscribe("events");
    }
}
