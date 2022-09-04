package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.api.CloudPlayer;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import de.leantwi.cloudsystem.proxy.server.BungeeConnector;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerConnectListener implements Listener {

    private final ProxyConnector proxyConnector = ProxyConnector.getInstance();
    private final BungeeConnector bungeeConnector = this.proxyConnector.getBungeeConnector();


    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerConnect(ServerConnectEvent event) {

        CloudPlayer cloudPlayer = CloudPlayer.getCloudPlayer(event.getPlayer().getUniqueId());
        if (event.getTarget().getName().equalsIgnoreCase("fallbackServer")) {
            if (event.getPlayer().hasPermission("cloud.use")) {
                cloudPlayer.sendMessage(ProxyConnector.getInstance().getCloudPrefix() + "Du bist mit dem Proxy-Server §e" + cloudPlayer.getProxyID() + " §7verbunden.");
            }
            event.setTarget(this.bungeeConnector.getLobbyServer());
            return;
        }

        cloudPlayer.setServerName(event.getTarget().getName());


    }

}
