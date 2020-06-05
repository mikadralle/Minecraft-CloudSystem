package eu.unyfy.master.handler.packets.handler;

import eu.unyfy.master.Master;
import lombok.Getter;

@Getter
public class PacketHandler {

  private final Master master = Master.getInstance();

  public void callPacket(Packet packet) {
    this.master.getExecutorService().execute(packet::execute);
  }
}
