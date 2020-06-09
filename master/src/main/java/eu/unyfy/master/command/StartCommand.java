package eu.unyfy.master.command;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.handler.hoster.HetnerType;
import eu.unyfy.service.command.CommandImplementation;

public class StartCommand implements CommandImplementation {

  @Override
  public void execute(String[] strings) {

    //start <cx11>

    if (strings.length == 1) {

      String type = null;

      MasterBootstrap.getInstance().getHosterCloud().createServer(HetnerType.CX11, "wrapper-" + MasterBootstrap.getInstance().getWrapperHandler().getID());

      MasterBootstrap.getInstance().sendMessage("Cloud start new wrapper server!");

    }

  }

  @Override
  public String getName() {
    return "start";
  }

  @Override
  public boolean isUsageRight(String[] strings) {
    return true;
  }

  @Override
  public String getUsage() {
    return "start hc server.";
  }
}
