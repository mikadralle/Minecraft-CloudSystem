package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.gameserver.UpdateGameServerStatusEvent;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import de.leantwi.cloudsystem.proxy.ProxyConnector;

public class GameTypeChangeListener implements Listener {

    @PacketListener
    public void onUpdateGameServerStatusEvent(UpdateGameServerStatusEvent event) {

        final String serverName = event.getServerName();
        final GameState gameState = event.getGameStatus();

        if (gameState == GameState.STARTED) {
            GameServerData gameServerData = CloudSystem.getAPI().getGameServerByServerName(serverName);
            ProxyConnector.getInstance().getProxyHandler().addServer(gameServerData.getServerName(), gameServerData.getHostName(), gameServerData.getPort());
            return;
        }
        if (gameState == GameState.SHUTDOWN) {

            ProxyConnector.getInstance().getProxyHandler().removeServer(serverName);
        }


    }


}

