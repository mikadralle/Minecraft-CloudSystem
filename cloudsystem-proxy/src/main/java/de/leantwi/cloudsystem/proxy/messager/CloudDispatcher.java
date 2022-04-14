package de.leantwi.cloudsystem.proxy.messager;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import io.nats.client.Dispatcher;
import net.md_5.bungee.api.ProxyServer;

import java.nio.charset.StandardCharsets;

public class CloudDispatcher {

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();

    public void listen() {

        Dispatcher cloudDispatcher = this.cloudSystemAPI.getNatsConnector().getConnection().createDispatcher(message -> {

            final String msg = new String(message.getData(), StandardCharsets.UTF_8);
            final String[] split = msg.split("#");
            final String serverName = split[1];

            switch (split[0]) {
                case "login_bungeecord":
                    notifyOnline(serverName);
                    break;
            }

        });
        cloudDispatcher.subscribe("cloud");

    }

    private void notifyOnline(String bungeeName) {
        ProxyServer.getInstance().getPlayers().stream().filter(players -> players.hasPermission("cloud.use")).forEach(players -> players.sendMessage(ProxyConnector.getInstance().getCloudPrefix() + "The §b" + bungeeName + " §7server is now §aonline§7!  "));
    }
}
