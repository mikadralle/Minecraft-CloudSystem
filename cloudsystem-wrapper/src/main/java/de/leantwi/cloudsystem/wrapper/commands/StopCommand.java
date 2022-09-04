package de.leantwi.cloudsystem.wrapper.commands;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;
import de.leantwi.service.command.CommandImplementation;

public class StopCommand implements CommandImplementation {

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();


    @Override
    public void execute(String[] strings) {

        final WrapperBootstrap wrapperBootstrap = WrapperBootstrap.getInstance();

        //stop <server|group|sub-Group> <name>

        if (strings.length == 0) {
            WrapperBootstrap.getInstance().onShutdown();
            sleep(1000);
            System.exit(0);
            return;
        }
        if (strings.length == 2) {

            final String name = strings[1].toLowerCase();

            switch (strings[0]) {

                case "group":
                    //doo
                    break;
                case "subgroup":
                    //dd
                    break;
                case "server":
                    if (!this.cloudSystemAPI.existsGameServerByServerName(name)) {
                        wrapperBootstrap.getLogger().info("§cThis server doesn't exists!");
                        StringBuilder stringBuilder = new StringBuilder();
                        this.cloudSystemAPI.getAllGameServers().forEach(server -> stringBuilder.append(server).append(", "));
                        wrapperBootstrap.getLogger().info("§aAll available servers: " + stringBuilder);
                        return;
                    }
                    cloudSystemAPI.getNatsConnector().publish("wrapper", "stop_server#" + name);
                    wrapperBootstrap.getLogger().info("§aServer §c'" + name + "'§a will be stopped ");
                    break;

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
        return "stop server or wrapper";
    }


    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
