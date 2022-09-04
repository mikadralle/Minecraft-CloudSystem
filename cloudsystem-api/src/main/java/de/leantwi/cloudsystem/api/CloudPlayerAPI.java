package de.leantwi.cloudsystem.api;

import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public abstract class CloudPlayerAPI {

    public String playerName, serverName, proxyID;
    public UUID uniqueID;

    public long lastJoinTime;

    public CloudPlayerAPI(String playerName, String serverName, String proxyID, UUID uuid, long lastJoinTime) {

        this.playerName = playerName;
        this.serverName = serverName;
        this.proxyID = proxyID;
        this.uniqueID = uuid;
        this.lastJoinTime = lastJoinTime;

    }


    public abstract void connect(String serverName);

    public abstract void connect(GameServerData gameServerData);


    public abstract String getGameServerName();

    public abstract boolean isOnline();

    public abstract String getProxyID();

    public abstract void sendMessage(String message);

    public abstract void kickPlayer(String message);


}
