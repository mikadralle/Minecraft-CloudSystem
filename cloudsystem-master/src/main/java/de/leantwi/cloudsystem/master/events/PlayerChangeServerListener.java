package de.leantwi.cloudsystem.master.events;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.PlayerChangeGameServerEvent;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import jdk.jfr.Enabled;

public class PlayerChangeServerListener implements Listener {

    @PacketListener
    public void onChangeServer(PlayerChangeGameServerEvent event){

        MasterBootstrap.getInstance().sendMessage("Previous Server: " + event.getPreviousServer());
    }
}
