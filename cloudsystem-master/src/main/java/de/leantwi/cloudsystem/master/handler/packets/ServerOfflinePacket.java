package de.leantwi.cloudsystem.master.handler.packets;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.packets.handler.Packet;
import de.leantwi.cloudsystem.master.handler.wrapper.WrapperHandler;
import lombok.Getter;

@Getter
public class ServerOfflinePacket extends Packet {

  private CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
  private final MasterBootstrap master = MasterBootstrap.getInstance();
  private final WrapperHandler wrapperHandler = this.master.getWrapperHandler();

  public ServerOfflinePacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    String serverName = getStrings()[1];


    this.master.sendMessage("The server " + serverName + " will be stopped");
    GameServerData gameServerData = this.cloudSystemAPI.getGameServerByServerName(serverName);
    gameServerData.setGameState(GameState.SHUTDOWN);
    this.cloudSystemAPI.updateGameServer(gameServerData);
    System.out.println("Server was part of " + gameServerData.getWrapperID());
    this.wrapperHandler.getWrapperServer("wrapper-1").removeServer(gameServerData);
    this.cloudSystemAPI.deleteGameServer(gameServerData);
  }

}
