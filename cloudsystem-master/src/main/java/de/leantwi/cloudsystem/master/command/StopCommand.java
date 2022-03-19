package de.leantwi.cloudsystem.master.command;

import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.service.command.CommandImplementation;

public class StopCommand implements CommandImplementation {

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(String[] strings) {

        final MasterBootstrap masterBootstrap = MasterBootstrap.getInstance();
        if (strings.length == 0) {

            masterBootstrap.onShutdown();
            masterBootstrap.sendMessage("The Master will be stopped now.");
            sleep(1000);
            System.exit(0);
            return;
        }
        if (strings.length == 1) {

            if (strings[0].equalsIgnoreCase("all")) {
                masterBootstrap.getNatsConnector().publish("cloud", "quit#quit");
                masterBootstrap.sendMessage("All gameservers and wrappers will be stopped now.");
                sleep(1000);
                masterBootstrap.sendMessage("The Master will be stopped now.");
                masterBootstrap.onShutdown();
                sleep(1000);
                System.exit(0);
            return;
            }


        }
        if(strings.length == 2){
            if(strings[0].equalsIgnoreCase("all") && strings[1].equalsIgnoreCase("gameserver")){
                masterBootstrap.getNatsConnector().publish("cloud", "quit#quit");
                masterBootstrap.sendMessage("All gameserver will be stopped now");
            }
        }

    }


    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public boolean isUsageRight(String[] strings) {
        return true;
    }

    @Override
    public String getUsage() {
        return "stop - Stop the master client";
    }
}
