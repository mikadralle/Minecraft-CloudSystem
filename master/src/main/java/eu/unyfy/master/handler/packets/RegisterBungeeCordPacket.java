package eu.unyfy.master.handler.packets;

import eu.unyfy.master.Master;
import eu.unyfy.master.database.redis.RedisConnector;
import eu.unyfy.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter

public class RegisterBungeeCordPacket extends Packet {

  private final Master master = Master.getInstance();
  private final RedisConnector redisConnector = master.getRedisConnector();
  //private Core core = master.getCore();


  public RegisterBungeeCordPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    //
    String bungeeName = getStrings()[1];
    this.master.getBungeeHandler().getBungeeList().add(bungeeName);

    Master.getInstance().getConsole().sendMessage("the bungeecord server '" + bungeeName + "' has been registered.");
    //  this.core.addBungeeCord(bungeeID);
    // this.redisConnector.sendMessage("accept_bungeecord#" + bungeeID + "#" + this.core.getServerCode());
    //    Master.getInstance().getConsole().sendMessage("The BungeeCord-" + bungeeID + " has been register.");

  }
}

