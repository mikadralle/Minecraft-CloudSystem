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

    public abstract void sendToServer(String serverName);

    public abstract String getCurrentServerName();

    public abstract boolean isOnline();


}
