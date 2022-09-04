package de.leantwi.cloudsystem.proxy.listeners.proxy;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.global.ShutdownSystemEvent;
import net.md_5.bungee.api.ProxyServer;

public class ShutdownSystemListener implements Listener {

    @PacketListener
    public void onShutdownSystemEvent(ShutdownSystemEvent event) {
        ProxyServer.getInstance().stop(event.getShutdownMessage());
    }

}
