package de.leantwi.cloudsystem.proxy.messager;
import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import io.nats.client.Dispatcher;
import net.md_5.bungee.api.ProxyServer;

import java.nio.charset.StandardCharsets;

public class BackendDispatcher {

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();

    public void listen() {

        Dispatcher backendDispatcher = this.cloudSystemAPI.getNatsConnector().getConnection().createDispatcher(message -> {
            final String msg = new String(message.getData(), StandardCharsets.UTF_8);
            final String[] split = msg.split("#");

            switch (split[0]) {

                case "stop":
                    if (ProxyConnector.getInstance().getProxyHandler().getBungeeName().equalsIgnoreCase(split[1])) {
                        ProxyServer.getInstance().stop("§cProxy is restarting!");
                    }
                    break;
                case "stop_all":
                    ProxyServer.getInstance().stop("§cProxy is restarting!");
                    break;
            }


        });

        backendDispatcher.subscribe("backend");

    }

}
