package eu.unyfy.master.command.commands;

import eu.unyfy.master.Master;
import eu.unyfy.master.api.console.Console;
import eu.unyfy.master.command.Command;
import eu.unyfy.master.database.redis.RedisConnector;
import lombok.Getter;

@Getter
public class StopCommand extends Command {

  private final Master instance = Master.getInstance();
  private final RedisConnector redisConnector = instance.getRedisConnector();

  public StopCommand() {
    super("stop");
  }

  @Override
  public void executeCommand(Console console, String[] strings) {
    if (!strings[0].equalsIgnoreCase("stop")) {
      return;
    }

    Master.getInstance().getConsole().sendMessage("Cloud is stopped", true);

    this.instance.getNatsConnector().sendMessage("cloud", "quit");
    sleep(1000);
    getRedisConnector().disconnect();
    Master.getInstance().getConsole().sendMessage("Redis connection has been disconnected");
    sleep(500);
    Master.getInstance().getConsole().sendMessage("Goodbye :-)");
    sleep(500);
    System.exit(0);
  }

  private void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
