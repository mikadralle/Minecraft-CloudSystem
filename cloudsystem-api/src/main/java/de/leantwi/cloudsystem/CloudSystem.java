package de.leantwi.cloudsystem;

import de.leantwi.cloudsystem.api.ICloudSystem;
import de.leantwi.cloudsystem.api.event.IEventHandler;
import lombok.Getter;
import lombok.Setter;

public class CloudSystem {

    private static ICloudSystem api;
    private static IEventHandler iEventHandler;

    public static ICloudSystem getAPI() {
        return api;
    }

    public static IEventHandler getEventAPI() {
        return iEventHandler;
    }

    public static void setAPI(ICloudSystem cloudSystem) {
        api = cloudSystem;
    }

    public static void setIEventAPI(IEventHandler eventAPI) {
        iEventHandler = eventAPI;
    }


}
