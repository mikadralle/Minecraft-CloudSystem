package eu.unyfy.master.handler.packets;

import eu.unyfy.master.Master;
import eu.unyfy.master.handler.core.Core;
import eu.unyfy.master.handler.packets.handler.Packet;
import eu.unyfy.master.handler.server.SessionServer;
import lombok.Getter;

@Getter
public class ServerOnlinePacket extends Packet {

  private final Master master = Master.getInstance();
  private final Core core = this.master.getCore();

  public ServerOnlinePacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    String[] strings = getStrings();
    String serverName = strings[1];

    SessionServer sessionServer = this.core.getSessionServer(serverName);
    System.out.println("name: " + sessionServer.getSubGroupDB().getSubGroupName());
    sessionServer.getSubGroupDB().getStartSessionServerList().remove(sessionServer);
    sessionServer.getSubGroupDB().getSessionServerList().add(sessionServer);

    // add this server in a redis online server list
    this.master.getServerFactory().addOnlineList(serverName);

    Master.getInstance().getConsole().sendMessage("The Server " + serverName + " is now online!");
  }

}
