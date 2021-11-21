package de.leantwi.cloudsystem.master.handler.packets;

import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class ServerStartPacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();

  /* private Core core = master.getCore();
   private ServerCore serverCore;
*/
  public ServerStartPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {
       /* serverCore = getCore().getServerCore(getStrings()[1]);

        Master.getInstance().getConsole().sendMessage("The Server " + serverCore.getServerName() + " is starting under port " + serverCore.getPort() + " at Wrapper-ID Wrapper-" + serverCore.getWrapperData().getWrapperID());


        */

  }
}