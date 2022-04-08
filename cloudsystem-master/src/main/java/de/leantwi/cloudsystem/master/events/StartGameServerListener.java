package de.leantwi.cloudsystem.master.events;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.gameserver.StartGameServerEvent;
import de.leantwi.cloudsystem.master.MasterBootstrap;

public class StartGameServerListener implements Listener {

    @PacketListener
    public void onStartGameServerEvent(StartGameServerEvent event){
        MasterBootstrap.getInstance().getLogger().info("GameServer: " + event.getGameServerName() + " is now online!");
    }

}
