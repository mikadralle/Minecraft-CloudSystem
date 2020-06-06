package eu.unyfy.cloudsystem;

import eu.unyfy.cloudsystem.command.CommandMap;
import eu.unyfy.cloudsystem.console.CloudConsole;
import eu.unyfy.cloudsystem.console.api.IConsole;
import eu.unyfy.cloudsystem.util.StringUtil;
import java.io.File;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Bootstrap {

  private final CloudConsole cloudConsole;

  private final IConsole bootstrapConsole;

  private final CommandMap commandMap;

  private boolean shutdown = false;

  public Bootstrap(final String consolePrefix) {

    final long timeStamp = System.currentTimeMillis();

    this.cloudConsole = new CloudConsole(consolePrefix);

    this.bootstrapConsole = cloudConsole.console(Bootstrap.class);

    this.commandMap = new CommandMap();

    final Thread thread = new Thread(() -> {

      final File dataFolder = new File("config");

      if (!dataFolder.exists()) {
        dataFolder.mkdirs();
      }

      //      final IConsole serviceProviderConsole = cloudConsole.console(ServiceProvider.class);
      //      StaticCommonClass.commonLoad(false, dataFolder, msg -> serviceProviderConsole.info(ServiceProvider.getPrefix() + msg));

      onBootstrap();

      Runtime.getRuntime().addShutdownHook(new Thread(() -> {

        getBootstrapConsole().info("§aShutting down...");

        onShutdown();

        //        StaticCommonClass.shutdown();

        getBootstrapConsole().info("Good bye...");
      }, "CloudSystem Shutdown Thread"));

      bootstrapConsole.info("§aInitialized in " + (System.currentTimeMillis() - timeStamp) + "ms. Type \"help\" for help!");

    }, "CloudSystem Initializer");

    thread.start();

    String line;

    try {
      while ((line = bootstrapConsole.readLine()) != null) {

        if (line.isEmpty() || StringUtil.isOnlyMemberOf(line, ' ')) {
          bootstrapConsole.info("Need a command");
          continue;
        }

        if (!commandMap.dispatchCommand(line)) {
          bootstrapConsole.warn("Command \"" + line + "\" not exist, try \"help\" for more information.");
        }
      }
    } catch (final Exception exception) {
      exception.printStackTrace();

      bootstrapConsole.error("Console is down?");
    }
  }

  public abstract void onBootstrap();

  public abstract void onShutdown();


}
