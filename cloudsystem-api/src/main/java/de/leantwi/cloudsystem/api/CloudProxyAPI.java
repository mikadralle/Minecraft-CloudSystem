package de.leantwi.cloudsystem.api;

import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class CloudProxyAPI {

    @Getter
    @Setter
    private long proxyStartTime;
    @Getter
    @Setter
    private String proxyID;


    public abstract List<CloudPlayerAPI> getAllProxyPlayers();

    public abstract void stopProxy(String shutdownMessage);

    public abstract List<GameServerData> getAllGameServers();


}
