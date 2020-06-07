package eu.unyfy.service.command;

import eu.unyfy.service.Service;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;


public class CommandHandler {


  private final Service service;
  @Getter
  private final List<CommandImplementation> commands = new ArrayList<>();

  public CommandHandler(Service service) {
    this.service = service;
  }

  public void handleInput(String input) {
    String[] splittedInput = input.split(" ");

    for (CommandImplementation commandImplementation : commands) {
      if (!commandImplementation.getName().equalsIgnoreCase(splittedInput[0])) {
        continue;
      }

      String[] args = new String[splittedInput.length - 1];

      if (splittedInput.length > 1) {
        System.arraycopy(splittedInput, 1, args, 0, splittedInput.length - 1);
      }

      if (!commandImplementation.isUsageRight(args)) {
        this.service.getLogger().warning("§cInvalid usage! §e" + commandImplementation.getUsage());
        return;
      }

      commandImplementation.execute(args);
      return;
    }

    this.service.getLogger().warning("§cInvalid command! Type 'help' for help!");
  }

  public void registerCommand(CommandImplementation commandImplementation) {
    this.commands.add(commandImplementation);
  }

}
