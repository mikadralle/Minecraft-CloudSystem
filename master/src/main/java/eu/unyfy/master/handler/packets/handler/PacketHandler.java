package eu.unyfy.master.handler.packets.handler;

import eu.unyfy.master.MasterBootstrap;
import lombok.Getter;

@Getter
public class PacketHandler {

  private final MasterBootstrap master = MasterBootstrap.getInstance();

  public void callPacket(Packet packet) {
    this.master.getExecutorService().execute(packet::execute);
  }
}
