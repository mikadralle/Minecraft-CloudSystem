package de.leantwi.cloudsystem.master.listeners;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.wrapper.UnRegisterWrapperEvent;
import de.leantwi.cloudsystem.master.MasterBootstrap;

public class UnRegisterWrapperListener implements Listener {

    @PacketListener
    public void onUnRegisterWrapperEvent(UnRegisterWrapperEvent event) {

        MasterBootstrap.getInstance().getWrapperHandler().logoutWrapper(event.getWrapperID());
        MasterBootstrap.getInstance().getLogger().info("The Wrapper " + event.getWrapperID() + " has been unregistered.");

    }
}
