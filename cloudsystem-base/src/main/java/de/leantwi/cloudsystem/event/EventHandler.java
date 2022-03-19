package de.leantwi.cloudsystem.event;

import com.google.gson.Gson;
import de.leantwi.cloudsystem.api.event.Event;
import de.leantwi.cloudsystem.api.event.IEventHandler;
import de.leantwi.cloudsystem.api.event.Listener;


public class EventHandler implements IEventHandler {

    private final EventBus eventBus;
    private final Gson gson = new Gson();

    public EventHandler() {
        this.eventBus = new EventBus();
    }

    @Override
    public void registerListener(Listener listener) {
        System.out.println("Jo");
        this.eventBus.register(listener);
        System.out.println("Jo-b");

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
