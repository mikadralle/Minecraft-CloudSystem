package eu.unyfy.master.command;

import eu.unyfy.cloudsystem.command.util.ICommand;
import eu.unyfy.master.MasterBootstrap;


public class HelpCommand implements ICommand {

  private final MasterBootstrap masterBootstrap = MasterBootstrap.getInstance();

  @Override
  public void onCommand(String[] strings) {

    if (strings.length == 0) {
      masterBootstrap.sendMessage("/stop could you stop the cloud system! ");
    }

  }
}
