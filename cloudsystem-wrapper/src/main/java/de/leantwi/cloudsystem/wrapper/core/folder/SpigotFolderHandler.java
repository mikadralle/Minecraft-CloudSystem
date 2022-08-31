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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

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

        List<String> argumentList = new ArrayList<>();
        argumentList.add("screen");
        argumentList.add("-AmdS");
        argumentList.add(serverName.toLowerCase());
        argumentList.add("java");
        argumentList.add("-Xmx" + memory + "M");
        //cloud configuration
        argumentList.add("-Dcloud.serverName=" + serverName.toLowerCase());
        //mongoDB configuration
        argumentList.add("-DmongoDB.hostname=");
        argumentList.add("-DmongoDB.port=");
        argumentList.add("-DmongoDB.username");
        argumentList.add("-DmongoDB.password");
        argumentList.add("-DmongoDB.authDB");
        argumentList.add("-DmongoDB.defaultDB");
        //redis configuration
        argumentList.add("-Dredis.hostname");
        argumentList.add("-Dredis.port");
        argumentList.add("-Dredis.password");
        argumentList.add("-Dredis.databaseID");
        //nats configuration
        argumentList.add("-Dnats.hostname");
        argumentList.add("-Dnats.token");
        argumentList.add("-jar");
        argumentList.add("spigot.jar");

        argumentList.add("--port");
        argumentList.add(String.valueOf(gameServerData.getPort()));
        argumentList.add("--host");
        argumentList.add(gameServerData.getHostName());

        ProcessBuilder processBuilder = new ProcessBuilder(argumentList);
        processBuilder.directory(new File(this.LIVE_PATH + "/" + this.gameServerData.getGroupDB() + "/" + this.gameServerData.getSubGroupDB() + "/" + serverName + "/"));
        //TODO: Hinzufügen wäre nice:
        //   processBuilder.redirectOutput(logsFile);
        //  processBuilder.redirectError(logsFile);
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                WrapperBootstrap.getInstance().getGameServerHandler().getProcessMap().put(this.gameServerData.getServerName(), processBuilder.start());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


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
