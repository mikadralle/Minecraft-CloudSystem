package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudPlayer;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import de.leantwi.cloudsystem.proxy.server.BungeeConnector;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerConnectListener implements Listener {

    private final ProxyConnector proxyConnector = ProxyConnector.getInstance();
    private final BungeeConnector bungeeConnector = this.proxyConnector.getBungeeConnector();


    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerConnect(ServerConnectEvent event) {

        if (event.getTarget().getName().equalsIgnoreCase("fallbackServer")) {
            ServerInfo serverInfo = this.bungeeConnector.getLobbyServer();

            if (serverInfo == null) {
                event.setCancelled(true);
                event.getPlayer().disconnect("§cCould not find central server!");
                return;
            }
            event.setTarget(serverInfo);
            return;
        }
        CloudPlayer cloudPlayer = CloudPlayer.getCloudPlayer(event.getPlayer().getUniqueId());
        cloudPlayer.setServerName(event.getTarget().getName());
        CloudSystem.getAPI().updateCloudPlayer(cloudPlayer);


    }

}
