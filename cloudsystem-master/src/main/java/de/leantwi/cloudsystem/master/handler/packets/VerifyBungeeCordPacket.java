package de.leantwi.cloudsystem.master.handler.packets;

import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.packets.handler.Packet;
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

    this.master.getNatsConnector().publish(replayTo, bungeeName);
    this.master.sendMessage("the bungeecord server '" + bungeeName + "' has been registered.");

  }
}

