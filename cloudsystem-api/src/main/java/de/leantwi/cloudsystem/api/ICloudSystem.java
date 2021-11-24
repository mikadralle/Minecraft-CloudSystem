package de.leantwi.cloudsystem.api;

import de.leantwi.cloudsystem.api.event.IEventHandler;

public interface ICloudSystem {

    IEventHandler getEventHandler();

}
