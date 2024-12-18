package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudPlayer;
import de.leantwi.cloudsystem.api.events.player.CloudPlayerQuitNetworkEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogoutListener implements Listener {
    private final ExecutorService executorService = Executors.newCachedThreadPool();


    @EventHandler
    public void onPlayerQuitEvent(PlayerDisconnectEvent event) {


        this.executorService.execute(() -> CloudSystem.getAPI().deleteCloudPlayer(CloudPlayer.getCloudPlayer(event.getPlayer().getUniqueId())));
        CloudSystem.getEventAPI().callEvent(new CloudPlayerQuitNetworkEvent(event.getPlayer().getUniqueId()));

    }
}
