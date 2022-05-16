package de.leantwi.cloudsystem.api;

public abstract class CloudPlayerAPI {

    abstract void sendToServer(String serverName);

    abstract String getCurrentServerName();

    abstract  boolean isOnline();


}
