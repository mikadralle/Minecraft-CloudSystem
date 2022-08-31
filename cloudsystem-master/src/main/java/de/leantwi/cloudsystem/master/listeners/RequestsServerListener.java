package de.leantwi.cloudsystem.master.listeners;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.master.RequestsServerEvent;
import de.leantwi.cloudsystem.master.MasterBootstrap;

public class RequestsServerListener implements Listener {

    @PacketListener
    public void onRequestServerListener(RequestsServerEvent event) {

        for (int i = 0; i < event.getAmount(); i++) {
            MasterBootstrap.getInstance().getServerFactory().createServer(event.getSubGroupName());
        }
    }
}
