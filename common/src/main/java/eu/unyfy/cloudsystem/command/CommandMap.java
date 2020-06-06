package eu.unyfy.cloudsystem.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import eu.unyfy.cloudsystem.command.util.ICommand;
import java.util.Map;


public final class CommandMap {

    private final Map<String, ICommand> commandMap = Maps.newConcurrentMap();

    public void registerCommand(final String command, final ICommand commandObject) {

        Preconditions.checkNotNull(command, "The command can not be null!");
        Preconditions.checkArgument(!command.isEmpty(), "The command can not be empty!");

        Preconditions.checkNotNull(commandObject, "The commandObject can not be null!");

        this.commandMap.put(command, commandObject);
    }

    public boolean dispatchCommand(final String line) {

        final String[] args = line.toLowerCase().split(" ");

        if (commandMap.containsKey(args[0])) {

            final String newLine;

            if (line.startsWith(args[0] + " ")) {
                newLine = line.replace(args[0] + " ", "");
            } else {
                newLine = line.replace(args[0], "");
            }

            final String[] newArgs;

            if (newLine.contains(" ")) {
                newArgs = newLine.split(" ");
            } else if (!newLine.isEmpty()) {
                newArgs = new String[]{newLine};
            } else {
                newArgs = new String[]{};
            }

            commandMap.get(args[0]).onCommand(newArgs);
            return true;
        }

        return false;
    }

    public Map<String, ICommand> getCommandMap() {
        return commandMap;
    }
}
