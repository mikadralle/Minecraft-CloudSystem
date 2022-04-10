package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.gameserver.StartGameServerEvent;
import net.md_5.bungee.api.ProxyServer;

public class StartGameServerListener implements Listener {

    @PacketListener
    public void onStartGameServerEvent(StartGameServerEvent event) {
        ProxyServer.getInstance().getConsole().sendMessage("GameServer " + event.getGameServerName() + " is now online!");
    }
}
