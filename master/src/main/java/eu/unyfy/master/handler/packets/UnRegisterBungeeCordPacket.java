package eu.unyfy.master.handler.packets;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class UnRegisterBungeeCordPacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();

  public UnRegisterBungeeCordPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {
    String bungeeName = getStrings()[1];
    this.master.getBungeeHandler().removeBungeeCord(bungeeName);
    this.master.sendMessage("the bungeecord server '" + bungeeName + "' has been unregistered.");
  }
}
