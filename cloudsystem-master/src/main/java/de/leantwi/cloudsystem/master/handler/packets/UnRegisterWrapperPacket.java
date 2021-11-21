package de.leantwi.cloudsystem.master.handler.packets;

import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class UnRegisterWrapperPacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();

  public UnRegisterWrapperPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    //wrapper
    String wrapperID = getStrings()[1];

    this.master.getWrapperHandler().logoutWrapper(wrapperID);

  }
}
