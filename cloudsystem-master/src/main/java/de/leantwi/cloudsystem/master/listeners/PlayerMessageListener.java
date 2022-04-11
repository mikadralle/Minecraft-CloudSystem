package de.leantwi.cloudsystem.master.listeners;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.SendCloudMessageEvent;
import de.leantwi.cloudsystem.master.MasterBootstrap;

public class PlayerMessageListener implements Listener {

    @PacketListener
    public void onMesssageChannle(SendCloudMessageEvent event) {
        MasterBootstrap.getInstance().sendMessage("I am second alert: " + event.getMessage());
    }
}
