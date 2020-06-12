package eu.unyfy.master.handler.packets;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.handler.packets.handler.Packet;
import eu.unyfy.master.handler.wrapper.WrapperType;
import lombok.Getter;

@Getter
public class VerifyWrapperPacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();

  public VerifyWrapperPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {
    //hostName#type#weightClass#priority

    String wrapperID = getStrings()[1];
    WrapperType wrapperType = WrapperType.valueOf(getStrings()[2]);
    int weightClass = Integer.parseInt(getStrings()[3]);
    int priority = Integer.parseInt(getStrings()[4]);
    String replayTo = getStrings()[5];

    String hostName = this.master.getWrapperHandler().verifyWrapper(wrapperID, wrapperType, weightClass, priority);
    this.master.getNatsConnector().sendMessage(replayTo, hostName);

    switch (wrapperType) {
      case PRIVATE:
        this.master.sendMessage("The private wrapper " + wrapperID + " has been verified.");
        break;
      case PUBLIC:
        this.master.sendMessage("The public wrapper " + wrapperID + " has been verified.");
        break;
    }

  }
}
