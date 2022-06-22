package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudPlayer;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.player.CloudPlayerJoinNetworkEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloudPlayerJoinNetworkListener implements Listener {

    private final ExecutorService executorService = Executors.newCachedThreadPool();


    @PacketListener
    public void onCloudPlayerJoinNetworkEvent(CloudPlayerJoinNetworkEvent event) {

        if (CloudSystem.getAPI().existsCloudPlayer(event.getUniqueID())) {

            CloudPlayer cloudPlayer = (CloudPlayer) CloudSystem.getAPI().getCloudPlayerByUUID(event.getUniqueID());

            cloudPlayer.setLastJoin(System.currentTimeMillis());
            cloudPlayer.setPlayerName(event.getPlayerName());
            this.executorService.execute(() -> CloudSystem.getAPI().updateCloudPlayer(cloudPlayer));
        } else {

            CloudPlayer cloudPlayer = new CloudPlayer();

            cloudPlayer.setUniqueID(event.getUniqueID());
            cloudPlayer.setLastJoin(System.currentTimeMillis());
            cloudPlayer.setPlayerName(event.getPlayerName());

            this.executorService.execute(() -> CloudSystem.getAPI().updateCloudPlayer(cloudPlayer));

        }


    }
}
