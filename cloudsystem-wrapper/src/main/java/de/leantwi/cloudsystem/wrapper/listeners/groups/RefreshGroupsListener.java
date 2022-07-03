package de.leantwi.cloudsystem.wrapper.listeners.groups;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.groups.RefreshGroupEvent;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;

public class RefreshGroupsListener implements Listener {

    @PacketListener
    public void onRefreshGroupEvent(RefreshGroupEvent event) {
        CloudSystem.getAPI().refreshGroups();
        WrapperBootstrap.getInstance().getFolderUtils().createGroupFolder(event.getTargetGroupName(), event.getTargetSubGroupName());

    }
}
