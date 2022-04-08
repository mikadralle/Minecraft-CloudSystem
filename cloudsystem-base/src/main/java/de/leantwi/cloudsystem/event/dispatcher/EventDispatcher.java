package de.leantwi.cloudsystem.event.dispatcher;

import com.google.gson.Gson;
import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.event.Event;
import de.leantwi.cloudsystem.event.EventBus;
import de.leantwi.cloudsystem.event.EventHandler;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;

import java.nio.charset.StandardCharsets;


public class EventDispatcher {

    private Connection connection;
    private EventHandler eventHandler;
    private EventBus eventBus;

    public EventDispatcher(Connection connection, EventHandler eventHandler, EventBus eventBus) {
        this.connection = connection;
        this.eventHandler = eventHandler;
        this.eventBus = eventBus;
    }

    public void listen() {

        final Dispatcher eventDispatcher = connection.createDispatcher(message -> {
            final String msg = new String(message.getData(), StandardCharsets.UTF_8);
            System.out.println("MSG: " + msg);
            eventBus.post(eventHandler.readBuff(msg));
        });


        eventDispatcher.subscribe("events");
    }
}
