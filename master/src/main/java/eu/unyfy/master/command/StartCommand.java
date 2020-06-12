package eu.unyfy.master.command;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.handler.hoster.HetnerType;
import eu.unyfy.service.command.CommandImplementation;

public class StartCommand implements CommandImplementation {

  @Override
  public void execute(String[] strings) {

    //start <cx11>

    if (strings.length == 1) {

      String type = strings[0];
      MasterBootstrap.getInstance().getWrapperHandler().createServer(HetnerType.CX11);

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
