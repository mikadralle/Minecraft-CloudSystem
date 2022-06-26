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

    private final String tempPath = "temp/";
    private final String livePath = "live/";
    private final File DEFAULT_PLUGINS = new File("default/plugins/");
    private final File DEFAULT_VERSIONS = new File("default/versions");
    private final File DEFAULT_SERVER = new File("default/server");
    @Getter

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();

    public void load() {
        this.deleteFolder(new File("live/"));

        if (!existsFolder(tempPath)) {
            createFolder(tempPath);
        }
        if (!existsFolder(livePath)) {
            createFolder(livePath);
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
        this.checkForNewGroups();

    }

    public void createTemp(GameServerData gameServerData) {

        final String groupName = gameServerData.getGroupDB();
        final String subGroupName = gameServerData.getSubGroupDB();
        final String serverName = gameServerData.getServerName();

        if (!existsFolder(tempPath + groupName)) {
            createFolder(tempPath + groupName);
        }
        if (!existsFolder(tempPath + groupName + "/" + subGroupName)) {
            createFolder(tempPath + groupName + "/" + subGroupName);
        }
        deleteFolder(new File("live/" + groupName + "/" + subGroupName + "/" + serverName));
        if (!existsFolder("live/" + groupName + "/" + subGroupName + "/" + serverName)) {
            createFolder("live/" + groupName + "/" + subGroupName + "/" + serverName);
        }

        File temp_File = new File(tempPath + groupName + "/" + subGroupName + "/");
        File live_file = new File("live/" + "/" + groupName + "/" + subGroupName + "/" + serverName);
        try {
            FileUtils.copyDirectory(temp_File, live_file);
            FileUtils.copyDirectoryToDirectory(DEFAULT_PLUGINS, live_file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSpigotYML(gameServerData);
        setBukkitYML(gameServerData);
        setServerProperties(gameServerData);
        setCloudConfig(gameServerData);
    }


    public void setSpigotYML(GameServerData gameServerData) {
        ConfigAPI configAPI = new ConfigAPI("live/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName(), "spigot.yml", ": ");
        configAPI.delete();
        configAPI.createFile();
        configAPI.set("settings.bungeecord", true);
        configAPI.set("commands.tab-complete", 2);
        configAPI.set("world-settings.default.anti-xray.engine-mode", 2);

    }

    public void setBukkitYML(GameServerData gameServerData) {
        ConfigAPI configAPI = new ConfigAPI("live/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName(), "bukkit.yml", ": ");
        configAPI.delete();
        configAPI.createFile();
        configAPI.set("settings.allow-end", false);
        configAPI.set("settings.connection-throttle", -1);
    }

    public void setServerProperties(GameServerData gameServerData) {
        ConfigAPI configAPI = new ConfigAPI("live/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName(), "server.properties", "=");
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

    public void setCloudConfig(GameServerData gameServerData) {
        IniFile iniFile = new IniFile("live/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + gameServerData.getServerName() + "/cloud.ini");
        if (iniFile.isEmpty()) {
            iniFile.setProperty("serverName", gameServerData.getServerName());
            iniFile.saveToFile();
        }
    }

    private boolean existsFolder(String path) {
        final File file = new File(path);
        return file.exists();
    }

    private void createFolder(String path) {
        final File file = new File(path);
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
        File currentFolder = new File(tempPath + groupName + "/" + subGroupName);
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
