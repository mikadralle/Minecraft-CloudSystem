package de.leantwi.cloudsystem.proxy.server;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class BungeeConnector {

    private final ProxyConnector proxyConnector = ProxyConnector.getInstance();
    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();



    public ServerInfo getLobbyServer() {

        final Map<String, Integer> lobbies = new HashMap<>();

        List<GameServerData> gameServerData = this.cloudSystemAPI.getAllGameServerBySubGroupName("lobby");
        gameServerData.forEach(gameServer -> {
            lobbies.put(gameServer.getServerName(),gameServer.getOnlinePlayers());
        } );

        final List<Integer> list = new ArrayList<>();
        lobbies.keySet().forEach(name -> list.add(lobbies.get(name)));
        Collections.sort(list);
        int min = list.get(0);

        return ProxyServer.getInstance().getServerInfo(lobbies.keySet().stream().filter(name -> lobbies.get(name).equals(min)).findFirst().get());
    }



}
