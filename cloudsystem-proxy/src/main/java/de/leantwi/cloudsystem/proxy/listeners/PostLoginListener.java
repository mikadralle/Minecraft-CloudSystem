package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.events.player.CloudPlayerJoinNetworkEvent;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLoginEvent(PostLoginEvent event) {
        CloudSystem.getEventAPI().callEvent(new CloudPlayerJoinNetworkEvent(event.getPlayer().getUniqueId(), event.getPlayer().getName(), event.getPlayer().getServer().getInfo().getName(), ProxyConnector.getInstance().getCloudProxy().getProxyID()));
    }
}
