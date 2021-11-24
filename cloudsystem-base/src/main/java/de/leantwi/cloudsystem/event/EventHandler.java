package de.leantwi.cloudsystem.event;

import de.leantwi.cloudsystem.api.event.Event;
import de.leantwi.cloudsystem.api.event.IEventHandler;
import de.leantwi.cloudsystem.api.event.Listener;


public class EventHandler implements IEventHandler {

    private final EventBus eventBus;

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
        System.out.println("Ja");
        this.eventBus.post(event);
        System.out.println("Ja-b");
    }
}
