package de.leantwi.cloudsystem.master.listeners;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.PlayerChangeGameServerEvent;
import de.leantwi.cloudsystem.master.MasterBootstrap;

public class PlayerChangeServerListener implements Listener {

    @PacketListener
    public void onChangeServer(PlayerChangeGameServerEvent event){

        MasterBootstrap.getInstance().sendMessage("Previous Server: " + event.getPreviousServer());
    }
}
