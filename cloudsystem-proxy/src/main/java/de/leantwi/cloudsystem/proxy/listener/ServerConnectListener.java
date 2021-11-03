package de.leantwi.cloudsystem.proxy.listener;

import de.leantwi.cloudsystem.proxy.ProxyConnector;
import de.leantwi.cloudsystem.proxy.server.BungeeConnector;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerConnectListener implements Listener {

    private ProxyConnector proxyConnector = ProxyConnector.getInstance();
    private BungeeConnector bungeeConnector = this.proxyConnector.getBungeeConnector();



    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerConnect(ServerConnectEvent event) {
        if (event.getTarget().getName().equalsIgnoreCase("fallbackServer")) {
            event.setTarget(this.bungeeConnector.getLobbyServer());
            return;
        }
    }

}
