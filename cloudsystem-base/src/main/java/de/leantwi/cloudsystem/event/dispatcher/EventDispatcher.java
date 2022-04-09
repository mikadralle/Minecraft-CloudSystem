package de.leantwi.cloudsystem.event.dispatcher;

import de.leantwi.cloudsystem.api.event.Event;
import de.leantwi.cloudsystem.event.EventHandler;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;

import java.nio.charset.StandardCharsets;


public class EventDispatcher {

    private final Connection connection;
    private final EventHandler eventHandler;

    public EventDispatcher(Connection connection, EventHandler eventHandler) {
        this.connection = connection;
        this.eventHandler = eventHandler;
    }

    public void listen() {

        final Dispatcher eventDispatcher = connection.createDispatcher(message -> {
            final String msg = new String(message.getData(), StandardCharsets.UTF_8);
            final Event event = eventHandler.getEventByJsonKey(msg);
            this.eventHandler.postEvent(event);
        });


        eventDispatcher.subscribe("events");
    }
}
