package de.leantwi.cloudsystem.master.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.gameserver.GameTypeChangeEvent;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import de.leantwi.cloudsystem.master.MasterBootstrap;

public class GameTypeChangeListener implements Listener {

    @PacketListener
    public void onGameTypeChangeListener(GameTypeChangeEvent event) {

        final String serverName = event.getServerName();
        final GameState gameState = GameState.getGameStateByString(event.getGametype());

        if (gameState == GameState.LOBBY) {
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
