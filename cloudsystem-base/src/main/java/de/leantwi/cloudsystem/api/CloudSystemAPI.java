package de.leantwi.cloudsystem.api;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.event.IEventHandler;

public class CloudSystemAPI implements ICloudSystem {
    @Override
    public IEventHandler getEventHandler() {
        return CloudSystem.getEventAPI();
    }
}
