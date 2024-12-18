package de.leantwi.cloudsystem.wrapper.database.message;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;
import io.nats.client.Dispatcher;

import java.nio.charset.StandardCharsets;

public class CloudDispatcher {

    private final WrapperBootstrap wrapper = WrapperBootstrap.getInstance();
    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    public void listen() {

        Dispatcher cloudDispatcher = this.cloudSystemAPI.getNatsConnector().getConnection().createDispatcher(message -> {

            final String msg = new String(message.getData(), StandardCharsets.UTF_8);
            final String[] split = msg.split("#");


            switch (split[0]) {
                case "stop":
                    //stop all
                    if (split[1].equalsIgnoreCase("all") || split[1].equalsIgnoreCase("wrapper") || split[1].equalsIgnoreCase("master")) {
                        WrapperBootstrap.getInstance().onShutdown();
                        sleep(1000);
                        System.exit(0);
                        break;
                    }
                    WrapperBootstrap.getInstance().setMasterOnline(false);
                    break;
            }

        });

        cloudDispatcher.subscribe("cloud");

    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
