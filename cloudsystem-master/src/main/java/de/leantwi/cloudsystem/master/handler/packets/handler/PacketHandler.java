package de.leantwi.cloudsystem.master.handler.packets.handler;

import de.leantwi.cloudsystem.master.MasterBootstrap;
import lombok.Getter;

@Getter
public class PacketHandler {

  private final MasterBootstrap master = MasterBootstrap.getInstance();

  public void callPacket(Packet packet) {
    this.master.getExecutorService().execute(packet::execute);
  }
}
