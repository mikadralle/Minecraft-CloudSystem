package eu.unyfy.wrapper.commands;


import eu.unyfy.service.command.CommandImplementation;
import eu.unyfy.wrapper.WrapperBootstrap;

/**
 * Class created by qlow | Jan
 */
public class HelpCommand implements CommandImplementation {

  @Override
  public void execute(String[] args) {
    WrapperBootstrap.getInstance().getCommandHandler().getCommands().forEach(commandImplementation -> WrapperBootstrap.getInstance().sendMessage(commandImplementation.getUsage()));
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
