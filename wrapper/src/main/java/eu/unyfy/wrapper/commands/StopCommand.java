package eu.unyfy.wrapper.commands;

import eu.unyfy.service.command.CommandImplementation;
import eu.unyfy.wrapper.WrapperBootstrap;
import eu.unyfy.wrapper.core.WrapperCore;

public class StopCommand implements CommandImplementation {

  @Override
  public void execute(String[] strings) {

    final WrapperBootstrap wrapperBootstrap = WrapperBootstrap.getInstance();
    final WrapperCore wrapperCore = wrapperBootstrap.getWrapperCore();

    //stop <server|group|sub-Group> <name>

    if (strings.length == 0) {
      WrapperBootstrap.getInstance().onShutdown();
      sleep(1000);
      System.exit(0);
      return;
    }
    if (strings.length == 2) {

      final String name = strings[1].toLowerCase();

      switch (strings[0]) {

        case "group":
          //doo
          break;
        case "subgroup":
          //dd
          break;
        case "server":
          if (!wrapperCore.existsServer(name)) {
            wrapperBootstrap.getLogger().info("§cThis server doesn't exists!");
            StringBuilder stringBuilder = new StringBuilder();
            wrapperCore.getOnlineSession().keySet().forEach(server -> stringBuilder.append(server).append(", "));
            wrapperBootstrap.getLogger().info("§aAll available servers: " + stringBuilder.toString());
            return;
          }
          wrapperBootstrap.getNatsConnector().sendMessage("wrapper", "stop_server#" + name);
          wrapperBootstrap.getLogger().info("§aServer §c'" + name + "'§a will be stopped ");
          break;

      }

    }


  }

  @Override
  public String getName() {
    return "stop";
  }

  @Override
  public boolean isUsageRight(String[] strings) {
    return true;
  }

  @Override
  public String getUsage() {
    return "stop server or wrapper";
  }


  private void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
