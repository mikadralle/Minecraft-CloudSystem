package de.leantwi.cloudsystem.master.handler.packets;

import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.packets.handler.Packet;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PlayerDisconnectNetworkPacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();
  //  private Player player;

  public PlayerDisconnectNetworkPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    UUID uuid = UUID.fromString(getStrings()[1]);
    this.master.sendMessage("The Player " + uuid + " disconnected from the network.");
  }
}
