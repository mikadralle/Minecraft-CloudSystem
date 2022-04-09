package de.leantwi.cloudsystem.api.event;

public interface EventHandlerAPI {

    void registerListener(Listener listener);

    void callEvent(Event event);

    void postEvent(Event event);

}
