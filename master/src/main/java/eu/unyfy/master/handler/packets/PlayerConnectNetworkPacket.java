package eu.unyfy.master.handler.packets;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.handler.packets.handler.Packet;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PlayerConnectNetworkPacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();
  //private Player player;

  public PlayerConnectNetworkPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    UUID uuid = UUID.fromString(getStrings()[1]);
    int bungeeID = Integer.parseInt(getStrings()[2]);
/*
        player = getMaster().getPlayer();
        player.setBungeeID(uuid, bungeeID);
*/
    this.master.sendMessage("The Player " + uuid + " connected to BungeeCord-" + bungeeID);

  }

}
