package de.leantwi.cloudsystem.proxy.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectListener implements Listener {


    @EventHandler
    public void onLoginEvent(LoginEvent event) {
    /*    if (event.isCancelled()) {

            return;
        }

        if (ProxyServer.getInstance().getServers().keySet().stream().noneMatch(server -> server.contains("lobby-"))) {
            event.setCancelled(true);
            event.setCancelReason("§cCould not find a central server!");
            return;
        }
     */
        System.out.println("DEBUG: 5");


    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {

        ProxyServer.getInstance().getConsole().sendMessage("TARGET: " + event.getTarget().getName());
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event) {

        ProxyServer.getInstance().getConsole().sendMessage("Name: " + event.getPlayer().getName());
        ProxyServer.getInstance().getConsole().sendMessage("Server: " + event.getPlayer().getServer().getInfo().getName());
    }

}
