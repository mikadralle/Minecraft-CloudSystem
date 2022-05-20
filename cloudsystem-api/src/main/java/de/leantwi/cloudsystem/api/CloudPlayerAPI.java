package de.leantwi.cloudsystem.api;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public abstract class CloudPlayerAPI {

    private String playerName, currentServerName, proxyID;
    private UUID uniqueID;

    private long lastJoin;

    abstract void sendToServer(String serverName);

    abstract String getCurrentServerName();

    abstract boolean isOnline();


}
