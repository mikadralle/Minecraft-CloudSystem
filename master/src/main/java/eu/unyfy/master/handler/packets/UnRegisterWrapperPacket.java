package eu.unyfy.master.handler.packets;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.handler.packets.handler.Packet;
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
