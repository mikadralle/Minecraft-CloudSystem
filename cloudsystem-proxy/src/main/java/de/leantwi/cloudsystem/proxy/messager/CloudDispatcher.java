package de.leantwi.cloudsystem.proxy.messager;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import de.leantwi.cloudsystem.proxy.server.BungeeConnector;
import io.nats.client.Dispatcher;
import net.md_5.bungee.api.ProxyServer;

import java.nio.charset.StandardCharsets;

public class CloudDispatcher {

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    private final ProxyConnector proxyConnector = ProxyConnector.getInstance();

    public void listen() {

        Dispatcher cloudDispatcher = this.cloudSystemAPI.getNatsConnector().getConnection().createDispatcher(message -> {

            final String msg = new String(message.getData(), StandardCharsets.UTF_8);
            final String[] split = msg.split("#");
            final String serverName = split[1];

            switch (split[0]) {
                case "login_bungeecord":
                    notifyOnline(serverName);
                    break;
                case "offline":
                    this.proxyConnector.getProxyHandler().removeServer(serverName);
                    break;
                case "online":
                    GameServerData gameServerData = CloudSystem.getAPI().getGameServerByServerName(serverName);
                    this.proxyConnector.getProxyHandler().addServer(gameServerData.getServerName(), gameServerData.getHostName(), gameServerData.getPort());
                    break;
                case "logout_bungeecord":
                    notifyOffline(serverName);
                    break;
            }

        });
        cloudDispatcher.subscribe("cloud");

    }

    private void notifyOnline(String bungeeName) {
        ProxyServer.getInstance().getPlayers().stream().filter(players -> players.hasPermission("cloud.use")).forEach(players -> players.sendMessage(ProxyConnector.getInstance().getCloudPrefix() + "The §b" + bungeeName + " §7server is now §aonline§7!  "));
    }

    private void notifyOffline(String bungeeName) {
        ProxyServer.getInstance().getPlayers().stream().filter(players -> players.hasPermission("cloud.use")).forEach(players -> players.sendMessage(ProxyConnector.getInstance().getCloudPrefix() + "The §b" + bungeeName + " §7server is now §coffline§7!  "));
    }
}
