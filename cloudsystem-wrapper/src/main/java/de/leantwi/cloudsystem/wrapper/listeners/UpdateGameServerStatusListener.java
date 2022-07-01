package de.leantwi.cloudsystem.wrapper.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.gameserver.UpdateGameServerStatusEvent;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;
import de.leantwi.cloudsystem.wrapper.core.GameServerHandler;

public class UpdateGameServerStatusListener implements Listener {

    private final GameServerHandler gameServerHandler = WrapperBootstrap.getInstance().getGameServerHandler();
    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();

    @PacketListener
    public void onUpdateGameServerStatusEvent(UpdateGameServerStatusEvent event) {


        final GameState gameState = event.getGameStatus();

        if(gameState == GameState.STARTED){
            this.gameServerHandler.finishServer(event.getServerName());
        }




    }
}
