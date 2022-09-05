package de.leantwi.cloudsystem.proxy.listeners.players;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudPlayer;
import de.leantwi.cloudsystem.api.CloudPlayerAPI;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.player.CloudPlayerJoinNetworkEvent;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;


public class CloudPlayerJoinNetworkListener implements Listener {

    @PacketListener
    public void onCloudPlayerJoinNetworkEvent(CloudPlayerJoinNetworkEvent event) {

        CloudPlayerAPI cloudPlayer = new CloudPlayer(event.getPlayerName(), "fallbackServer", event.getProxyID(), event.getUniqueID(), System.currentTimeMillis());
        CloudSystem.getAPI().updateCloudPlayer(cloudPlayer);

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getUniqueID());
        if (player != null) {
            if (player.hasPermission("cloud.use")) {
                cloudPlayer.sendMessage(ProxyConnector.getInstance().getCloudPrefix() + "Du bist mit dem Proxy-Server §e" + cloudPlayer.getProxyID() + " §7verbunden.");
            }
        }


    }
}
