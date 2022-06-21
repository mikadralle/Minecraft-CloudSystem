package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudPlayer;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.player.CloudPlayerQuitNetworkEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloudPlayerQuitNetworkListener implements Listener {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @PacketListener
    public void onCloudPlayerQuitNetworkEvent(CloudPlayerQuitNetworkEvent event) {

        CloudPlayer cloudPlayer = (CloudPlayer) CloudSystem.getAPI().getCloudPlayerByUUID(event.getUniqueID());
        cloudPlayer.setLastJoin(System.currentTimeMillis());

        this.executorService.execute(() -> CloudSystem.getAPI().updateCloudPlayer(cloudPlayer));
        this.executorService.execute(() -> CloudSystem.getAPI().deleteCloudPlayer(cloudPlayer));

    }
}
