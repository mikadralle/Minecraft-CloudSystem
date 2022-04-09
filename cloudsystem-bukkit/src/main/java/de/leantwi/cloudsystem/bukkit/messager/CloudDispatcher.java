package de.leantwi.cloudsystem.bukkit.messager;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.bukkit.BukkitConnector;
import de.leantwi.cloudsystem.bukkit.server.SpigotConnector;
import io.nats.client.Dispatcher;
import org.bukkit.Bukkit;

import java.nio.charset.StandardCharsets;

public class CloudDispatcher {
    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();

    public void listen() {
        final Dispatcher cloudDispatcher = this.cloudSystemAPI.getNatsConnector().getConnection().createDispatcher(message -> {

            final String msg = new String(message.getData(), StandardCharsets.UTF_8);
            final String[] split = msg.split("#");

            System.out.println("Message: " + msg);

            final String serverName = split[1].toLowerCase();
            final SpigotConnector spigotConnector = BukkitConnector.getInstance().getSpigotConnector();
            switch (split[0]) {

                case "quit":
                    stopServer();
                    break;
                case "stop":
                    if (spigotConnector.getServerName().equalsIgnoreCase(serverName)) {
                        Bukkit.shutdown();
                    }
                    break;
                default:
                    break;
            }


        });
        cloudDispatcher.subscribe("cloud");
    }


    private void stopServer() {
        Bukkit.getScheduler().runTask(BukkitConnector.getInstance(), Bukkit::shutdown);
    }

}
