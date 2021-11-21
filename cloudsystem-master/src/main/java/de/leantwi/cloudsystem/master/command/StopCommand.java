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
        masterBootstrap.onShutdown();
        sleep(1000);
        System.exit(0);
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
