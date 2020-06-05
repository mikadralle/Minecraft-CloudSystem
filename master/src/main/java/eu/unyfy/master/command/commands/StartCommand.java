package eu.unyfy.master.command.commands;

import eu.unyfy.master.Master;
import eu.unyfy.master.api.console.Console;
import eu.unyfy.master.command.Command;
import eu.unyfy.master.handler.core.Core;
import lombok.Getter;

@Getter
public class StartCommand extends Command {

  private final Master instance = Master.getInstance();
  private final Core core = instance.getCore();

  public StartCommand() {
    super("start");
  }

  @Override
  public void executeCommand(Console console, String[] strings) {
    if (!strings[0].equalsIgnoreCase("start")) {
      return;
    }
/*
        //start <Group> <amount>

        if (strings.length == 3) {

            final String groupName = strings[1].toLowerCase();
            final int amount = Integer.parseInt(strings[2]);

            String result = getCore().exitsGroup(groupName);

            if (result.equalsIgnoreCase("null")) {
                instance.getConsole().sendMessage("Die Gruppe " + groupName + " existiert nicht!", true);
                return;
            }

            ServerData serverData = getCore().getServerData(result);

            for (int i = 0; i < amount; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getCore().startServer(serverData.getGroupData(), serverData);
            }
            instance.getConsole().sendMessage("Du hast " + amount + " Server aus der Gruppe " + groupName + " gestartet!", true);
            return;
        }
        instance.getConsole().sendMessage("usage: start <group-name> <amount>", true);
 */
  }


}
