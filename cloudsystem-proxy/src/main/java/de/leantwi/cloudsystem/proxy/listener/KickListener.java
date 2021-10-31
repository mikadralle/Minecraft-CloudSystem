package de.leantwi.cloudsystem.proxy.listener;

import de.leantwi.cloudsystem.proxy.ProxyConnector;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class KickListener implements Listener {

    @EventHandler
    public void onKickListener(ServerKickEvent event) {
        event.setCancelServer(ProxyConnector.getInstance().getBungeeConnector().getLobbyServer());
    }


}
