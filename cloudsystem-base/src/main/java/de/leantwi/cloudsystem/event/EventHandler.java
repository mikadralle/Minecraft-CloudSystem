package de.leantwi.cloudsystem.event;

import com.google.gson.Gson;
import de.leantwi.cloudsystem.api.event.Event;
import de.leantwi.cloudsystem.api.event.EventHandlerAPI;
import de.leantwi.cloudsystem.api.event.Listener;


public class EventHandler implements EventHandlerAPI {

    private final EventBus eventBus;
    private final Gson gson = new Gson();

    public EventHandler() {
        this.eventBus = new EventBus();
    }

    @Override
    public void registerListener(Listener listener) {
        this.eventBus.register(listener);

    }

    @Override
    public void callEvent(Event event) {

        System.out.println("broadcast event");
        String value = gson.toJson(event);
        System.out.println("JSON: " + value);
        this.eventBus.post(event);

    }

    @Override
    public void postEvent(Event event) {
        System.out.println("psot event: " + event.getClass().getName());
        this.eventBus.post(event);

    }

}
