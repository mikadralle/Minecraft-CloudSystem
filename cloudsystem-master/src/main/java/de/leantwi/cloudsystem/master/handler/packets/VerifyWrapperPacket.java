package de.leantwi.cloudsystem.master.handler.packets;

import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.packets.handler.Packet;
import de.leantwi.cloudsystem.master.handler.wrapper.WrapperType;
import lombok.Getter;

import java.util.Arrays;

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
    this.master.sendMessage("String: " + Arrays.toString(getStrings()));
    this.master.sendMessage("ReplayTO: " + replayTo);

    String hostName = this.master.getWrapperHandler().verifyWrapper(wrapperID, wrapperType, weightClass, priority);
    this.master.getNatsConnector().publish(replayTo, hostName);

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
