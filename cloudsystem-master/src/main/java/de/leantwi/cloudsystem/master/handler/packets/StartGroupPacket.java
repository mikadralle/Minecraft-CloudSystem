package de.leantwi.cloudsystem.master.handler.packets;

import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.core.Core;
import de.leantwi.cloudsystem.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class StartGroupPacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();

  private final Core core = this.master.getCore();

  public StartGroupPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    final String subGroupName = getStrings()[1];

    for (int i = 0; i < Integer.parseInt(getStrings()[2]); i++) {
      this.master.getServerFactory().createServer(subGroupName);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }
  }


}