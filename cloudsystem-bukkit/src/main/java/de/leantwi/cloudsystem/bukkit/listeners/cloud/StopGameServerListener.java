package de.leantwi.cloudsystem.bukkit.listeners.cloud;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.gameserver.StopGameServerEvent;
import de.leantwi.cloudsystem.bukkit.BukkitConnector;
import org.bukkit.Bukkit;

public class StopGameServerListener implements Listener {

    @PacketListener
    public void onStopGameServerEvent(StopGameServerEvent event) {
        if (BukkitConnector.getInstance().getSpigotConnector().getServerName().equalsIgnoreCase(event.getServerName())) {
            Bukkit.getScheduler().runTaskLater(BukkitConnector.getInstance(), Bukkit::shutdown, 5);
        }
    }

}
