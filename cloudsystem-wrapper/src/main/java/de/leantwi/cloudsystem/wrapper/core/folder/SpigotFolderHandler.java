package de.leantwi.cloudsystem.wrapper.core.folder;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.wrapper.SetupServerHandlerAPI;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;
import de.leantwi.cloudsystem.wrapper.core.FolderUtils;
import de.leantwi.cloudsystem.wrapper.utils.config.ConfigAPI;
import de.leantwi.cloudsystem.wrapper.utils.config.IniFile;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
public class SpigotFolderHandler implements SetupServerHandlerAPI {


    private final FolderUtils folderUtils = WrapperBootstrap.getInstance().getFolderUtils();
    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    private GameServerData gameServerData;
    private Process process;
    private File LIVE_PATH;
    private File TEMP_PATH;

    public SpigotFolderHandler(GameServerData gameServerData) {
        this.gameServerData = gameServerData;

        this.LIVE_PATH = this.folderUtils.getPath(gameServerData);
        this.TEMP_PATH = this.folderUtils.TEMP_PATH;

    }


    @Override
    public void setup() {

        File temp = new File(this.TEMP_PATH + "/" + this.gameServerData.getGroupDB() + "/" + this.gameServerData.getSubGroupDB() + "/");
        File live = new File(this.LIVE_PATH + "/" + this.gameServerData.getGroupDB() + "/" + this.gameServerData.getSubGroupDB() + "/" + this.gameServerData.getServerName());

        temp.mkdirs();

        if (this.gameServerData.isStaticMode()) {

            if (live.exists()) {
                setServerProperties(this.gameServerData);
                setCloudConfig(this.gameServerData);
                return;
            }

        }
        this.folderUtils.deleteFolder(live);
        live.mkdirs();

        try {
            FileUtils.copyDirectory(temp, live);
            FileUtils.copyDirectoryToDirectory(this.folderUtils.DEFAULT_PLUGINS, live);
        } catch (IOException e) {
            e.printStackTrace();
        }


        setSpigotYML(gameServerData);
        setBukkitYML(gameServerData);
        setServerProperties(gameServerData);
        setCloudConfig(gameServerData);

    }

    @Override
    public void startServer() {

        String serverName = this.gameServerData.getServerName();
        int memory = this.cloudSystemAPI.getSubGroupByName(this.gameServerData.getSubGroupDB()).get().getServerDB().getMemory();

        try {
            this.process = new ProcessBuilder(
                    "screen", "-AmdS", serverName.toLowerCase(),
                    "java", "-Xms" + memory + "M", "-Xmx" + memory + "M", "-jar", "spigot.jar")
                    .directory(new File(this.folderUtils.getPath(this.gameServerData) + "/" + this.gameServerData.getGroupDB() + "/" + this.gameServerData.getSubGroupDB() + "/" + serverName + "/")).inheritIO().start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        WrapperBootstrap.getInstance().getLogger().info("§aServer §e" + serverName + "§a will be started.");
    }

    @Override
    public void setSpigotYML(GameServerData gameServerData) {
        ConfigAPI configAPI = new ConfigAPI(this.LIVE_PATH + "/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName(), "spigot.yml", ": ");
        configAPI.delete();
        configAPI.createFile();
        configAPI.set("settings.bungeecord", true);
        configAPI.set("commands.tab-complete", 2);
        configAPI.set("world-settings.default.anti-xray.engine-mode", 2);
    }

    @Override
    public void setBukkitYML(GameServerData gameServerData) {
        ConfigAPI configAPI = new ConfigAPI(this.LIVE_PATH + "/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName(), "bukkit.yml", ": ");
        configAPI.delete();
        configAPI.createFile();
        configAPI.set("settings.allow-end", false);
        configAPI.set("settings.connection-throttle", -1);
    }

    @Override
    public void setServerProperties(GameServerData gameServerData) {
        ConfigAPI configAPI = new ConfigAPI(this.LIVE_PATH + "/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName(), "server.properties", "=");
        configAPI.delete();
        configAPI.createFile();
        configAPI.set("server-port", gameServerData.getPort());
        configAPI.set("announce-player-achievements", false);
        configAPI.set("max-players", gameServerData.getMaxOnlinePlayers());
        configAPI.set("spawn-protection", 0);
        configAPI.set("view-distance", 4);
        configAPI.set("online-mode", false);
        configAPI.set("motd", "Voting");

        configAPI.set("allow-nether=", false);
        configAPI.set("gamemode", 0);
        configAPI.set("difficulty", 0);
        configAPI.set("spawn-monsters", false);
        configAPI.set("spawn-npcs", false);
        configAPI.set("spawn-animals", false);
        configAPI.set("white-list", false);
        configAPI.set("server-name", gameServerData.getServerName());
    }

    @Override
    public void setCloudConfig(GameServerData gameServerData) {
        IniFile iniFile = new IniFile(this.LIVE_PATH + "/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName() + "/cloud.ini");
        if (iniFile.isEmpty()) {
            iniFile.setProperty("serverName", gameServerData.getServerName());
            iniFile.saveToFile();
        }
    }
}
