package de.leantwi.cloudsystem.api;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.database.INats;
import de.leantwi.cloudsystem.api.event.IEventHandler;

public class CloudSystemAPI implements ICloudSystem {

    private INats iNats;

    public CloudSystemAPI(INats iNats) {
        this.iNats = iNats;


       this.iNats.connect();
    }

    @Override
    public IEventHandler getEventHandler() {
        return CloudSystem.getEventAPI();
    }

    @Override
    public INats getNatsConnector() {
        return this.iNats;
    }
}
