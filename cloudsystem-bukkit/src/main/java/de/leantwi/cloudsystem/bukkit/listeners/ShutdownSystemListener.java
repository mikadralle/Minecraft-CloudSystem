package de.leantwi.cloudsystem.bukkit.listeners;

import de.leantwi.cloudsystem.api.event.Listener;
import de.leantwi.cloudsystem.api.event.PacketListener;
import de.leantwi.cloudsystem.api.events.global.ShutdownSystemEvent;
import org.bukkit.Bukkit;

public class ShutdownSystemListener implements Listener {

    @PacketListener
    public void onShutdownSystemEvent(ShutdownSystemEvent event) {
        Bukkit.getOnlinePlayers().forEach(players -> players.kickPlayer(event.getShutdownMessage()));
        Bukkit.getServer().shutdown();
    }
}
