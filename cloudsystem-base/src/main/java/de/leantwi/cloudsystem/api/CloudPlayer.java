package de.leantwi.cloudsystem.api;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.events.player.SendPlayerToServerEvent;

public  class CloudPlayer extends CloudPlayerAPI {




    @Override
    public void sendToServer(String serverName) {

        //  CloudSystem.getEventAPI().callEvent(new SendPlayerToServerEvent());
        //  CloudSystem.getEventAPI().callEvent(new SendPlayerToServerEvent());

    }

    @Override
    public String getCurrentServerName() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

}
