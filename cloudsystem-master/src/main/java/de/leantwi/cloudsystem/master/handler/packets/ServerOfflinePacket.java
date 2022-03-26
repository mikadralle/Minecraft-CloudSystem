package de.leantwi.cloudsystem.master.handler.packets;

import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.core.Core;
import de.leantwi.cloudsystem.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class ServerOfflinePacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();
  private final Core core = this.master.getCore();

  public ServerOfflinePacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    String serverName = getStrings()[1];
    // remove this server from redis online server list
    this.master.getServerFactory().removeOnlineList(serverName);

    this.master.sendMessage("The server " + serverName + " will be stopped");
    GameServerData sessionServer = this.core.getSessionServer(serverName);
    this.master.getServerFactory().deleteServer(sessionServer);

  }

}
