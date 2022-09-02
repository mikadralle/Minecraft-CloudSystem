package de.leantwi.cloudsystem;

import de.leantwi.cloudsystem.api.CloudPlayerAPI;

import java.util.List;

public interface CloudProxy {

    String getProxyID();

    List<CloudPlayerAPI> getAllProxyPlayers();

    void stopProxy(String shutdownMessage);

}
