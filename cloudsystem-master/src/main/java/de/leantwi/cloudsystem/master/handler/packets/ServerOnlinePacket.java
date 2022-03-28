package de.leantwi.cloudsystem.master.handler.packets;

import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.core.Core;
import de.leantwi.cloudsystem.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class ServerOnlinePacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();
  private final Core core = this.master.getCore();

  public ServerOnlinePacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    String[] strings = getStrings();
    String serverName = strings[1];

    GameServerData sessionServer = this.core.getSessionServer(serverName);
    System.out.println("name: " + sessionServer.getSubGroupDB());

    // add this server in a redis online server list
    this.master.getServerFactory().addOnlineList(serverName);

    this.master.sendMessage("The Server " + serverName + " is now online!");
  }

}
