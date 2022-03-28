package de.leantwi.cloudsystem;

import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.event.EventHandlerAPI;

public class CloudSystem {

    private static CloudSystemAPI api;
    private static EventHandlerAPI iEventHandler;

    public static CloudSystemAPI getAPI() {
        return api;
    }

    public static EventHandlerAPI getEventAPI() {
        return iEventHandler;
    }

    public static void setAPI(CloudSystemAPI cloudSystem) {
        api = cloudSystem;
    }

    public static void setIEventAPI(EventHandlerAPI eventAPI) {
        iEventHandler = eventAPI;
    }


}
