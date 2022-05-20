package de.leantwi.cloudsystem.api;

public class CloudPlayer extends CloudPlayerAPI {


    @Override
    public void sendToServer(String serverName) {

    }

    @Override
    String getCurrentServerName() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return false;
    }
}
