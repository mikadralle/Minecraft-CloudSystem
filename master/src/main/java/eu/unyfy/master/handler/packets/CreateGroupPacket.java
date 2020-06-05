package eu.unyfy.master.handler.packets;

import eu.unyfy.master.Master;
import eu.unyfy.master.handler.group.GroupHandler;
import eu.unyfy.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class CreateGroupPacket extends Packet {

  private final Master master = Master.getInstance();
  private final GroupHandler groupHandler = this.master.getGroupHandler();
  //private Player player;

  public CreateGroupPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    String group = getStrings()[1];
    String subGroupName = getStrings()[2];

    // group#subgrouop
    this.groupHandler.createGroup(group, subGroupName);
    Master.getInstance().getConsole().sendMessage("the group " + group + " and " + subGroupName + " has been created.");

  }

}
