package de.leantwi.cloudsystem.master.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.gameserver.UpdateGameServerStatusEvent;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import de.leantwi.cloudsystem.master.MasterBootstrap;

public class GameTypeChangeListener implements Listener {

    @PacketListener
    public void onUpdateGameServerStatusEvent(UpdateGameServerStatusEvent event) {

        final String serverName = event.getServerName();
        final GameState gameState = event.getGameStatus();

        if (gameState == GameState.STARTED) {
            MasterBootstrap.getInstance().getLogger().info("the " + serverName + " is now §aonline§f.");
            return;
        }
        if(gameState == GameState.SHUTDOWN){

            GameServerData gameServerData = CloudSystem.getAPI().getGameServerByServerName(serverName);
            MasterBootstrap.getInstance().getLogger().info("the" + serverName + " is now §coffline§f.");
            MasterBootstrap.getInstance().getWrapperHandler().getWrapperServer("wrapper-1").removeServer(gameServerData);
            CloudSystem.getAPI().deleteGameServer(gameServerData);
        }

    }
}
