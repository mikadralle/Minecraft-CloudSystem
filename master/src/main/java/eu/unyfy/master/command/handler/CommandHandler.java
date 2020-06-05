package eu.unyfy.master.command.handler;

import eu.unyfy.master.Master;
import eu.unyfy.master.command.Command;
import eu.unyfy.master.command.commands.StartCommand;
import eu.unyfy.master.command.commands.StopCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandHandler {

  private final Master master = Master.getInstance();
  public List<Command> commands = new ArrayList<>();


  public void registerCommands() {

    addCommand(new StopCommand());
    addCommand(new StartCommand());

    Scanner scanner = new Scanner(System.in);
    while (true) {
      String line = scanner.nextLine();

      if (line.startsWith("Master")) {
        continue;
      }
      commands.forEach(command -> {
        command.executeCommand(master.getConsole(), line.split(" "));
      });
    }
  }


  private void addCommand(Command command) {

    this.commands.add(command);
  }


}
