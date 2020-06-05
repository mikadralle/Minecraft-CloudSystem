package eu.unyfy.master.handler.packets;

import eu.unyfy.master.Master;
import eu.unyfy.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class RegisterWrapperPacket extends Packet {

  private final Master master = Master.getInstance();

  public RegisterWrapperPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    System.out.println("provokant");
    //wrapper#memory
    String wrapperID = getStrings()[1];
    this.master.getWrapperHandler().loginWrapper(wrapperID, Integer.parseInt(getStrings()[2]), getStrings()[3]);
    Master.getInstance().getConsole().sendMessage("The wrapper " + wrapperID + " has been registered.");
  }
}
