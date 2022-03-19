package de.leantwi.cloudsystem.api;

import de.leantwi.cloudsystem.api.database.INats;
import de.leantwi.cloudsystem.api.event.IEventHandler;

public interface ICloudSystem {

    IEventHandler getEventHandler();
    INats getNatsConnector();
}
