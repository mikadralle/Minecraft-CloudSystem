package eu.unyfy.master.command;


import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.service.command.CommandImplementation;

/**
 * Class created by qlow | Jan
 */
public class HelpCommand implements CommandImplementation {

  @Override
  public void execute(String[] args) {
    MasterBootstrap.getInstance().getCommandHandler().getCommands().forEach(commandImplementation -> MasterBootstrap.getInstance().sendMessage(commandImplementation.getUsage()));
  }

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public boolean isUsageRight(String[] args) {
    return true;
  }

  @Override
  public String getUsage() {
    return "help - Shows the help menu";
  }
}
