package de.leantwi.cloudsystem.master.listeners;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.proxy.UnRegisterBungeeCordEvent;
import de.leantwi.cloudsystem.master.MasterBootstrap;

public class UnRegisterBungeeCordListener implements Listener {

    @PacketListener
    public void onUnRegisterBungeeCordEvent(UnRegisterBungeeCordEvent event) {

        MasterBootstrap.getInstance().getBungeeHandler().removeBungeeCord(event.getBungeeName());
        MasterBootstrap.getInstance().getLogger().info("The BungeeCord server " + event.getBungeeName() + " has benn unregistered.");

    }
}
