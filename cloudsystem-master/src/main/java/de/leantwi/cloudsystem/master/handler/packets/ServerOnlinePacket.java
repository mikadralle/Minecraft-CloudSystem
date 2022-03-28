package de.leantwi.cloudsystem.master.handler.packets;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class ServerOnlinePacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();

  private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();

  public ServerOnlinePacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    String[] strings = getStrings();
    String serverName = strings[1];

    GameServerData gameServerData = this.getCloudSystemAPI().getGameServerByServerName(serverName);
    gameServerData.setGameState(GameState.LOBBY);
    this.cloudSystemAPI.updateGameServer(gameServerData);

    this.master.sendMessage("The Server " + serverName + " is now online!");
  }

}
