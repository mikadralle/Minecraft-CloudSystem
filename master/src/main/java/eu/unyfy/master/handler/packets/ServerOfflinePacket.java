package eu.unyfy.master.handler.packets;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.handler.core.Core;
import eu.unyfy.master.handler.packets.handler.Packet;
import eu.unyfy.master.handler.server.SessionServer;
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
    SessionServer sessionServer = this.core.getSessionServer(serverName);
    this.master.getServerFactory().deleteServer(sessionServer);

  }

}
