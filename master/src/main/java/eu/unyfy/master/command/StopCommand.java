package eu.unyfy.master.command;

import eu.unyfy.cloudsystem.command.util.ICommand;
import eu.unyfy.master.MasterBootstrap;

public class StopCommand implements ICommand {

  private final MasterBootstrap masterBootstrap = MasterBootstrap.getInstance();

  @Override
  public void onCommand(String[] strings) {

    if (strings.length == 0) {
      this.masterBootstrap.sendMessage("Cloud is stopping.");
      sleep(100);
      this.masterBootstrap.getNatsConnector().sendMessage("cloud", "quit#all");
      sleep(1000);
      masterBootstrap.getRedisConnector().disconnect();
      masterBootstrap.sendMessage("Redis connection has been disconnected");
      masterBootstrap.getMongoDBConnector().disconnect();
      sleep(500);
      masterBootstrap.sendMessage("Goodbye :-)");
      sleep(500);
      System.exit(0);
    } else {
      masterBootstrap.getBootstrapConsole().info("use /stop");
    }

  }

  private void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
