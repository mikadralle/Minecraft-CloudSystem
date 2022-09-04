package de.leantwi.cloudsystem.proxy.listeners.players;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudPlayer;
import de.leantwi.cloudsystem.api.CloudPlayerAPI;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.player.CloudPlayerJoinNetworkEvent;


public class CloudPlayerJoinNetworkListener implements Listener {

    @PacketListener
    public void onCloudPlayerJoinNetworkEvent(CloudPlayerJoinNetworkEvent event) {

        CloudPlayerAPI cloudPlayer = new CloudPlayer(event.getPlayerName(), "fallbackServer", event.getProxyID(), event.getUniqueID(), System.currentTimeMillis());
        CloudSystem.getAPI().updateCloudPlayer(cloudPlayer);


    }
}
