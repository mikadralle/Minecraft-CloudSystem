package de.leantwi.cloudsystem.api;

import de.leantwi.cloudsystem.api.gameserver.GameServerData;

import java.util.List;

public interface CloudProxyAPI {

    long startProxyTime();

    String getProxyID();

    List<CloudPlayerAPI> getAllProxyPlayers();

    void stopProxy(String shutdownMessage);

    List<GameServerData> getAllGameServers();


}
