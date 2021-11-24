package de.leantwi.cloudsystem.master.events;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.SendCloudMessageEvent;
import de.leantwi.cloudsystem.master.MasterBootstrap;

public class MessageListener implements Listener {

    @PacketListener
    public void onMessageEvent(SendCloudMessageEvent event) {

        MasterBootstrap.getInstance().sendMessage(event.getMessage());

    }

}
