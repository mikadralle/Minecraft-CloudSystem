package de.leantwi.cloudsystem.proxy.listeners.players;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.player.SendMessageToCloudPlayerEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SendMessageToCloudPlayerListener implements Listener {

    @PacketListener
    public void onSendMessageToCloudPlayerEvent(SendMessageToCloudPlayerEvent event) {

        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getUuid());
        if (player != null) {
            player.sendMessage(event.getMessage());
        }
    }
}
