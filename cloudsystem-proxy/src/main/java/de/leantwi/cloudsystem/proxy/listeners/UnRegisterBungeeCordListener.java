package de.leantwi.cloudsystem.proxy.listeners;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.proxy.UnRegisterBungeeCordEvent;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import net.md_5.bungee.api.ProxyServer;

public class UnRegisterBungeeCordListener implements Listener {

    @PacketListener
    public void onUnRegisterBungeeCordEvent(UnRegisterBungeeCordEvent event){
        ProxyServer.getInstance().getPlayers().stream().filter(players -> players.hasPermission("cloud.use")).forEach(players -> players.sendMessage(ProxyConnector.getInstance().getCloudPrefix() + "The §b" + event.getBungeeName() + " §7server is now §coffline§7!  "));
    }
}
