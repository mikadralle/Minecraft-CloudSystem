package de.leantwi.cloudsystem.proxy.listeners.players;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.player.KickCloudPlayerEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class KickCloudPlayerListener implements Listener {

    @PacketListener
    public void onKickCloudPlayerEvent(KickCloudPlayerEvent event) {

        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getUuid());
        if (player != null) {
            player.disconnect(event.getKickMessage());
        }

    }
}
