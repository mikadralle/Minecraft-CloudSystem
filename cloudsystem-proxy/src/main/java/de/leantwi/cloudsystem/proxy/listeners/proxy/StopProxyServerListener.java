package de.leantwi.cloudsystem.proxy.listeners.proxy;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.proxy.StopProxyServerEvent;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import net.md_5.bungee.api.ProxyServer;

public class StopProxyServerListener implements Listener {

    @PacketListener
    public void onStopProxyServerEvent(StopProxyServerEvent event) {

        if (event.getProxyID().equalsIgnoreCase(ProxyConnector.getInstance().getCloudProxy().getProxyID())) {
            ProxyServer.getInstance().getPlayers().forEach(players -> players.disconnect("§cProxy is restarting!"));
            ProxyServer.getInstance().stop(event.getShutdownMessage());
        }

    }
}
