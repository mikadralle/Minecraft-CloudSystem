package de.leantwi.cloudsystem.proxy.handler;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudPlayerAPI;
import de.leantwi.cloudsystem.api.CloudProxyAPI;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.events.proxy.UnRegisterBungeeCordEvent;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class CloudProxy extends CloudProxyAPI {

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    private final String prefix = ProxyConnector.getInstance().getCloudPrefix();

    public CloudProxy() {

        setProxyStartTime(System.currentTimeMillis());
        this.loginProxyServer();

    }

    @Override
    public List<CloudPlayerAPI> getAllProxyPlayers() {
        return null;
    }

    @Override
    public void stopProxy(String shutdownMessage) {

        ProxyServer.getInstance().stop(shutdownMessage);

    }

    @Override
    public List<GameServerData> getAllGameServers() {
        return cloudSystemAPI.getAllGameServers();
    }


    private void loginProxyServer() {
        try {
            setProxyID(this.cloudSystemAPI.getNatsConnector().request("verify", "bungeecord_register#"));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }


        this.cloudSystemAPI.getAllGameServers().stream().
                filter(gameServerData -> gameServerData.getGameState() != GameState.STARTS && gameServerData.getGameState() != GameState.SHUTDOWN).
                forEach(gameServerData -> {
                    addServer(gameServerData.getServerName(), gameServerData.getHostName(), gameServerData.getPort());
                });

        this.cloudSystemAPI.updateCloudProxy(this);
        /*
        this.cloudSystemAPI.getAllGameServer().forEach(gameServerData -> {
            addServer(gameServerData.getServerName(), gameServerData.getHostName(), gameServerData.getPort());
        });

         */
    }

    public void logoutProxyServer() {

        CloudSystem.getEventAPI().callEvent(new UnRegisterBungeeCordEvent(this.getProxyID()));
        ProxyServer.getInstance().getPlayers().forEach(players -> players.disconnect(this.prefix + "§cProxy will be restarted!"));

        this.cloudSystemAPI.deleteCloudProxy(this);

    }


    public void addServer(String serverName, String address, int port) {
        if (ProxyServer.getInstance().getServers().containsKey(serverName)) {
            this.removeServer(serverName);
        }
        ProxyServer.getInstance().getServers().put(serverName, ProxyServer.getInstance().constructServerInfo(serverName, InetSocketAddress.createUnresolved(address, port), "-", false));
        ProxyServer.getInstance().getPlayers().stream().filter(players -> players.hasPermission("cloud.use")).forEach(players -> players.sendMessage(this.prefix + "The server " + serverName + " §7is now §aonline§7."));
        ProxyServer.getInstance().getConsole().sendMessage(this.prefix + "The server " + serverName + " §7is now §aonline§7.");
    }

    public void removeServer(String serverName) {

        ServerInfo kickTo = ProxyConnector.getInstance().getBungeeConnector().getLobbyServer();
        ProxyServer.getInstance().getServerInfo(serverName).getPlayers().forEach(players -> players.connect(kickTo));
        ProxyServer.getInstance().getServers().remove(serverName);
        ProxyServer.getInstance().getPlayers().stream().filter(players -> players.hasPermission("cloud.use")).forEach(players -> players.sendMessage(this.prefix + "The server " + serverName + " §7is now §coffline§7."));
        ProxyServer.getInstance().getConsole().sendMessage(this.prefix + "The server " + serverName + " §7is now §coffline§7.");
    }
}
