package de.leantwi.cloudsystem.master.command;


import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.service.command.CommandImplementation;

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
        return "help - Lets show you all commands";
    }
}
