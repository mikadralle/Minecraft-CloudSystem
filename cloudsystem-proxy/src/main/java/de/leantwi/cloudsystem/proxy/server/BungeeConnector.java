package de.leantwi.cloudsystem.proxy.server;

import de.leantwi.cloudsystem.proxy.ProxyConnector;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class BungeeConnector {

    private final ProxyConnector proxyConnector = ProxyConnector.getInstance();
    //
    //map
    private final Map<String, ServerFactory> serverFactoryMap = new HashMap<>();


    public ServerFactory getServerFactory(String serverName) {
        return serverFactoryMap.get(serverName);
    }

    public void createServer(String serverName) {
        if (!this.serverFactoryMap.containsKey(serverName)) {
            ServerFactory serverFactory = new ServerFactory(serverName);
            serverFactory.registerServer();

            this.serverFactoryMap.put(serverName, serverFactory);
        }
    }

    public void removeServer(String serverName) {
        this.serverFactoryMap.get(serverName).unRegisterServer();
        this.serverFactoryMap.remove(serverName);
    }

    public ServerInfo getLobbyServer() {

        final Map<String, Integer> lobbies = new HashMap<>();
        List<String> lobbyListName = getLobbyServerList();

        lobbyListName.forEach(serverName -> lobbies.put(serverName, getServerFactory(serverName).getSessionServer().getOnlinePlayers()));

        final List<Integer> list = new ArrayList<>();
        lobbies.keySet().forEach(name -> list.add(lobbies.get(name)));
        Collections.sort(list);
        int min = list.get(0);

        return ProxyServer.getInstance().getServerInfo(lobbies.keySet().stream().filter(name -> lobbies.get(name).equals(min)).findFirst().get());
    }

    public List<String> getLobbyServerList() {
        return ProxyServer.getInstance().getServers().keySet().stream().filter(serverName -> serverName.startsWith("lobby-")).collect(Collectors.toList());
    }


}
