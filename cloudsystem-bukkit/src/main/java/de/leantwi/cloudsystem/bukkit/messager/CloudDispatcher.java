package de.leantwi.cloudsystem.bukkit.messager;

import de.leantwi.cloudsystem.bukkit.BukkitConnector;
import de.leantwi.cloudsystem.bukkit.database.NatsConnector;
import de.leantwi.cloudsystem.bukkit.server.SpigotConnector;
import io.nats.client.Dispatcher;
import org.bukkit.Bukkit;

import java.nio.charset.StandardCharsets;

public class CloudDispatcher {
    private final NatsConnector natsConnector = BukkitConnector.getInstance().getNatsConnector();

    public void listen() {
        final Dispatcher cloudDispatcher = this.natsConnector.getNats().createDispatcher(message -> {

            final String msg = new String(message.getData(), StandardCharsets.UTF_8);
            final String[] split = msg.split("#");

            System.out.println("Message: " + msg);

            final String serverName = split[1].toLowerCase();
            final SpigotConnector spigotConnector = BukkitConnector.getInstance().getSpigotConnector();
            switch (split[0]) {

                case "updateGameState":
                    spigotConnector.getServerFactory(serverName).updateGameType(split[2]);
                    break;
                case "online":
                    spigotConnector.registerServer(serverName);
                    break;
                case "offline":
                    spigotConnector.unRegisterServer(serverName);
                    break;
                case "quit":
                    Bukkit.getScheduler().runTask(BukkitConnector.getInstance(), Bukkit::shutdown);
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


}
