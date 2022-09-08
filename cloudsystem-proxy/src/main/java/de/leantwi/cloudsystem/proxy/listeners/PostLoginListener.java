package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudPlayer;
import de.leantwi.cloudsystem.api.CloudPlayerAPI;
import de.leantwi.cloudsystem.api.events.player.CloudPlayerJoinNetworkEvent;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLoginEvent(PostLoginEvent event) {

        ProxiedPlayer player = event.getPlayer();
        final String proxyID = ProxyConnector.getInstance().getCloudProxy().getProxyID();

        CloudPlayerAPI cloudPlayer = new CloudPlayer(player.getName(), "fallbackServer", proxyID, player.getUniqueId(), System.currentTimeMillis());
        CloudSystem.getAPI().updateCloudPlayer(cloudPlayer);

        if (player.hasPermission("cloud.use")) {
            cloudPlayer.sendMessage(ProxyConnector.getInstance().getCloudPrefix() + "Du bist mit dem Proxy-Server §e" + cloudPlayer.getProxyID() + " §7verbunden.");
        }

        CloudSystem.getEventAPI().callEvent(new CloudPlayerJoinNetworkEvent(player.getUniqueId(), player.getName(), proxyID));
    }
}
