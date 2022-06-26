package de.leantwi.cloudsystem.master.handler.packets;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class CreateGroupPacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();
  //private Player player;

  public CreateGroupPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {

    String group = getStrings()[1];
    String subGroupName = getStrings()[2];

    // group#subgrouop
    CloudSystem.getAPI().createGroup(group,subGroupName);
    this.master.sendMessage("the group " + group + " and " + subGroupName + " has been created.");

  }

}
