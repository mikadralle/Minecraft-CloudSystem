package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectedListener implements Listener {

    @EventHandler
    public void onServerConnectedEvent(ServerConnectedEvent event) {

        CloudPlayer cloudPlayer = CloudPlayer.getCloudPlayer(event.getPlayer().getUniqueId());
        cloudPlayer.setServerName(event.getServer().getInfo().getName());
        CloudSystem.getAPI().updateCloudPlayer(cloudPlayer);

    }
}
