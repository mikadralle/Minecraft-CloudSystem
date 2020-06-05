package eu.unyfy.master.command;

import eu.unyfy.master.Master;
import eu.unyfy.master.api.console.Console;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class Command {

  private final String command;
  private final Console console = Master.getInstance().getConsole();

  public abstract void executeCommand(Console console, String[] strings);


}
