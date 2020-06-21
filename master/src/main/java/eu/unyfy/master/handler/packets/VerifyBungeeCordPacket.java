package eu.unyfy.master.handler.packets;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter

public class VerifyBungeeCordPacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();


  public VerifyBungeeCordPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    String replayTo = getStrings()[1];
    String bungeeName = this.master.getBungeeHandler().verifyBungeeCord();

    this.master.getNatsConnector().sendMessage(replayTo, bungeeName);
    this.master.sendMessage("the bungeecord server '" + bungeeName + "' has been registered.");

  }
}

