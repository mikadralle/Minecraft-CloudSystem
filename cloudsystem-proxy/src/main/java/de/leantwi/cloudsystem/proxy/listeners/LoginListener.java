package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.events.player.CloudPlayerJoinNetworkEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

    @EventHandler
    public void onLoginEvent(LoginEvent event) {
        CloudSystem.getEventAPI().callEvent(new CloudPlayerJoinNetworkEvent(event.getConnection().getUniqueId(), event.getConnection().getName(), "bungeecord-01"));
    }
}
