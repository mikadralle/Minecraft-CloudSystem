package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.events.player.CloudPlayerQuitNetworkEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LogoutListener implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerDisconnectEvent event) {
        CloudSystem.getEventAPI().callEvent(new CloudPlayerQuitNetworkEvent(event.getPlayer().getUniqueId()));

    }
}
