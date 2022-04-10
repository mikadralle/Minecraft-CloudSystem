package de.leantwi.cloudsystem.wrapper.listeners;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.global.ShutdownSystemEvent;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;


public class ShutdownSystemListener implements Listener {

    @PacketListener
    public void onShutdownSystemEvent(ShutdownSystemEvent event){

        WrapperBootstrap.getInstance().onShutdown();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
