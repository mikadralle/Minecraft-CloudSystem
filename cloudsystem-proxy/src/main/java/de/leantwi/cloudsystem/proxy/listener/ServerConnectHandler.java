package de.leantwi.cloudsystem.proxy.listener;

import de.leantwi.cloudsystem.proxy.ProxyConnector;
import de.leantwi.cloudsystem.proxy.server.BungeeConnector;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectHandler implements Listener {

    private ProxyConnector proxyConnector = ProxyConnector.getInstance();
    private BungeeConnector bungeeConnector = this.proxyConnector.getBungeeConnector();

    @EventHandler
    public void onServerConnectEvent(ServerConnectEvent event) {
        if (event.getTarget().getName().equalsIgnoreCase("fallbackServer")) {
            event.setTarget(this.bungeeConnector.getLobbyServer());
        }
    }
}
