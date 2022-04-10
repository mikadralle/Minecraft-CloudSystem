package de.leantwi.cloudsystem.wrapper.listeners;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.gameserver.RequestGameServerEvent;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;

public class RequestGameServerListener implements Listener {

    @PacketListener
    public void onRequestGameServerEvent(RequestGameServerEvent event){

        if(!WrapperBootstrap.getInstance().getWrapperSettings().getWrapperID().equalsIgnoreCase(event.getWrapperID())){
            return;
        }
        WrapperBootstrap.getInstance().getWrapperCore().addRequestedGameServer(event.getServerName());
        WrapperBootstrap.getInstance().getLogger().info("The server " + event.getServerName() + " request has been accepted.");

    }
}
