package de.leantwi.cloudsystem.bukkit.server;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.events.gameserver.UpdateGameServerStatusEvent;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import lombok.Getter;

@Getter
public class SpigotConnector {

    //
    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    private String serverName;
    private GameServerData gameServerData;


    public void loginSpigotServer() {

        this.serverName = System.getProperty("cloud.serverName");
        this.gameServerData = this.cloudSystemAPI.getGameServerByServerName(this.serverName);
        this.gameServerData.setGameState(GameState.STARTED);

        CloudSystem.getAPI().updateGameServer(gameServerData);
        CloudSystem.getEventAPI().callEvent(new UpdateGameServerStatusEvent(gameServerData.getGameState().getName(), gameServerData.getServerName()));
    }

    public void logoutSpigotServer() {

        this.gameServerData.setGameState(GameState.SHUTDOWN);
        CloudSystem.getAPI().updateGameServer(gameServerData);
        CloudSystem.getEventAPI().callEvent(new UpdateGameServerStatusEvent(gameServerData.getGameState().getName(), gameServerData.getServerName()));

    }

}
