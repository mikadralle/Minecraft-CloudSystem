package de.leantwi.cloudsystem.bukkit.server;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.bukkit.config.IniFile;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class SpigotConnector {

    //
    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    private String serverName;


    public void loginSpigotServer() {
        final IniFile iniFile = new IniFile("cloud.ini");
        this.serverName = iniFile.getProperty("serverName");
        this.cloudSystemAPI.getNatsConnector().publish("cloud", "online#" + this.serverName);
        GameServerData gameServerData = this.cloudSystemAPI.getGameServerByServerName(serverName);
        Bukkit.getConsoleSender().sendMessage("§aSlots: " + gameServerData.getMaxOnlinePlayers());
    }

    public void logoutSpigotServer() {
        this.cloudSystemAPI.getNatsConnector().publish("cloud", "offline#" + this.serverName);
    }

}
