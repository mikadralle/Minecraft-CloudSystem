package de.leantwi.cloudsystem.proxy.listeners.players;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudPlayer;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.player.CloudPlayerQuitNetworkEvent;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloudPlayerQuitNetworkListener implements Listener {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @PacketListener
    public void onCloudPlayerQuitNetworkEvent(CloudPlayerQuitNetworkEvent event) {

        CloudPlayer cloudPlayer = CloudPlayer.getCloudPlayer(event.getUniqueID());

        ProxyServer.getInstance().getConsole().sendMessage("QUIT: " + cloudPlayer.getPlayerName());
        this.executorService.execute(() -> CloudSystem.getAPI().deleteCloudPlayer(cloudPlayer));

    }
}
