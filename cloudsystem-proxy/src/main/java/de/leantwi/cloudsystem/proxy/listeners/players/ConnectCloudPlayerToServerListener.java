package de.leantwi.cloudsystem.proxy.listeners.players;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.player.ConnectCloudPlayerToServerEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ConnectCloudPlayerToServerListener implements Listener {

    @PacketListener
    public void onConnectCloudPlayerToServerEvent(ConnectCloudPlayerToServerEvent event) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getUuid());
        if (player != null) {
            player.connect(ProxyServer.getInstance().getServerInfo(event.getTargetServerName()));
        }


    }
}
