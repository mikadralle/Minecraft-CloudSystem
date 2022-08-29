package de.leantwi.cloudsystem.wrapper.core;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;
import de.leantwi.cloudsystem.wrapper.utils.config.ConfigAPI;
import de.leantwi.cloudsystem.wrapper.utils.config.IniFile;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FolderUtils {

    private final File TEMP_PATH = new File("temp/");
    private final File LIVE_PATH = new File("live/");
    private final File LIVE_STATIC_PATH = new File("static/");
    private final File DEFAULT_PLUGINS = new File("default/plugins/");
    private final File DEFAULT_VERSIONS = new File("default/versions");
    private final File DEFAULT_SERVER = new File("default/server");
    @Getter

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();

    public void load() {
        this.deleteFolder(new File("live/"));

        if (!TEMP_PATH.exists()) {
            TEMP_PATH.mkdirs();
        }
        if (!LIVE_PATH.exists()) {
            LIVE_PATH.mkdirs();
        }

        if (!DEFAULT_PLUGINS.exists()) {
            DEFAULT_PLUGINS.mkdirs();
        }
        if (!DEFAULT_VERSIONS.exists()) {
            DEFAULT_VERSIONS.mkdirs();
            WrapperBootstrap.getInstance().getLogger().warning("the default version folder is §cempty§f.");
        }

        if (!DEFAULT_SERVER.exists()) {

            DEFAULT_SERVER.mkdirs();
            WrapperBootstrap.getInstance().getLogger().warning("the default server folder is §cempty§f.");

        }

        //load new groups folder.
        this.

                checkForNewGroups();

    }

    public void createTemp(GameServerData gameServerData) {

        final File currentLivePath = getPath(gameServerData);
        final String groupName = gameServerData.getGroupDB();
        final String subGroupName = gameServerData.getSubGroupDB();
        final String serverName = gameServerData.getServerName();

        final File temp_File = new File(this.TEMP_PATH + "/" + groupName + "/" + subGroupName + "/");
        final File live_file = new File(currentLivePath + "/" + groupName + "/" + subGroupName + "/" + serverName);

        //TODO: TESTEN
        //create folders for GameServer
        this.createFolder(temp_File);

        if (!gameServerData.isStaticMode()) {
            this.deleteFolder(live_file);
            this.createFolder(live_file);
        } else {
            if (this.existsFolder(live_file)) {
                setServerProperties(gameServerData, currentLivePath);
                setCloudConfig(gameServerData, currentLivePath);
                return;
            }
            this.createFolder(live_file);
            WrapperBootstrap.getInstance().getLogger().info("The static folder " + gameServerData.getServerName() + " has been created. ");

        }


        try {
            FileUtils.copyDirectory(temp_File, live_file);
            FileUtils.copyDirectoryToDirectory(DEFAULT_PLUGINS, live_file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSpigotYML(gameServerData, currentLivePath);
        setBukkitYML(gameServerData, currentLivePath);
        setServerProperties(gameServerData, currentLivePath);
        setCloudConfig(gameServerData, currentLivePath);
    }


    public void setSpigotYML(GameServerData gameServerData, File livePath) {
        ConfigAPI configAPI = new ConfigAPI(livePath + "/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName(), "spigot.yml", ": ");
        configAPI.delete();
        configAPI.createFile();
        configAPI.set("settings.bungeecord", true);
        configAPI.set("commands.tab-complete", 2);
        configAPI.set("world-settings.default.anti-xray.engine-mode", 2);

    }

    public void setBukkitYML(GameServerData gameServerData, File livePath) {
        ConfigAPI configAPI = new ConfigAPI(livePath + "/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName(), "bukkit.yml", ": ");
        configAPI.delete();
        configAPI.createFile();
        configAPI.set("settings.allow-end", false);
        configAPI.set("settings.connection-throttle", -1);
    }

    public void setServerProperties(GameServerData gameServerData, File livePath) {
        ConfigAPI configAPI = new ConfigAPI(livePath + "/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName(), "server.properties", "=");
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

    public void setCloudConfig(GameServerData gameServerData, File livePath) {
        IniFile iniFile = new IniFile(livePath + "/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName() + "/cloud.ini");
        if (iniFile.isEmpty()) {
            iniFile.setProperty("serverName", gameServerData.getServerName());
            iniFile.saveToFile();
        }
    }


    public File getPath(GameServerData gameServerData) {

        if (cloudSystemAPI.getSubGroupByName(gameServerData.getSubGroupDB()).get().getServerDB().isStaticMode()) {
            return LIVE_STATIC_PATH;
        }
        return LIVE_PATH;

    }

    private boolean existsFolder(File file) {
        return file.exists();
    }

    private void createFolder(File file) {
        file.mkdir();
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            File[] arrayOfFile1;
            int j = (arrayOfFile1 = files).length;
            for (int i = 0; i < j; i++) {
                File files2 = arrayOfFile1[i];
                if (files2.isDirectory()) {
                    deleteFolder(files2);
                } else {
                    files2.delete();
                }
            }
        }
        folder.delete();
    }


    private void checkForNewGroups() {


        WrapperBootstrap.getInstance().getLogger().info("Checking and loading groups ...");


        this.cloudSystemAPI.getAllGroups().forEach(groups -> {

            groups.getSubGroupDBList().forEach(subGroupDB -> {
                createGroupFolder(groups.getGroupName(), subGroupDB.getSubGroupName());
            });


        });


        WrapperBootstrap.getInstance().getLogger().info("all groups has been loaded.");

    }

    public void createGroupFolder(String groupName, String subGroupName) {
        File currentFolder = new File(this.TEMP_PATH + "/" + groupName + "/" + subGroupName);
        if (!currentFolder.exists()) {
            currentFolder.mkdirs();

            try {
                //copy the default server folder to new current subgroup folder
                FileUtils.copyDirectory(this.DEFAULT_SERVER, currentFolder);
                WrapperBootstrap.getInstance().getLogger().info("the template from group " + groupName + " has been created.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

}
