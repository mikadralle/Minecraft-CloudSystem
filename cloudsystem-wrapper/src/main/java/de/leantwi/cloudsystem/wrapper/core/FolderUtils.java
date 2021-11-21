package de.leantwi.cloudsystem.wrapper.core;

import de.leantwi.cloudsystem.wrapper.core.server.SessionServer;
import de.leantwi.cloudsystem.wrapper.utils.config.ConfigAPI;
import de.leantwi.cloudsystem.wrapper.utils.config.IniFile;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class FolderUtils {

  private final String tempPath = "temp/";
  private final String livePath = "live/";
  private final File DEFAULT_PLUGINS = new File("default/");

  public void load() {
    deleteFolder(new File("live/"));

    if (!existsFolder(tempPath)) {
      createFolder(tempPath);
    }
    if (!existsFolder(livePath)) {
      createFolder(livePath);
    }
    File file = new File("default/plugins/");
    if (!file.exists()) {
      file.mkdirs();
    }


  }

  public void createTemp(SessionServer sessionServer) {

    final String groupName = sessionServer.getGroupName();
    final String subGroupName = sessionServer.getSubGroupName();
    final String serverName = sessionServer.getServerName();

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
      FileUtils.copyDirectory(DEFAULT_PLUGINS, live_file);
    } catch (IOException e) {
      e.printStackTrace();
    }

    setSpigotYML(sessionServer);
    setBukkitYML(sessionServer);
    setServerProperties(sessionServer);
    setCloudConfig(sessionServer);
  }


  public void setSpigotYML(SessionServer sessionServer) {
    ConfigAPI configAPI = new ConfigAPI("live/" + sessionServer.getGroupName() + "/" + sessionServer.getSubGroupName() + "/" + sessionServer.getServerName(), "spigot.yml", ": ");
    configAPI.delete();
    configAPI.createFile();
    configAPI.set("settings.bungeecord", true);
    configAPI.set("commands.tab-complete", 2);
    configAPI.set("world-settings.default.anti-xray.engine-mode", 2);

  }

  public void setBukkitYML(SessionServer sessionServer) {
    ConfigAPI configAPI = new ConfigAPI("live/" + sessionServer.getGroupName() + "/" + sessionServer.getSubGroupName() + "/" + sessionServer.getServerName(), "bukkit.yml", ": ");
    configAPI.delete();
    configAPI.createFile();
    configAPI.set("settings.allow-end", false);
    configAPI.set("settings.connection-throttle", -1);
  }

  public void setServerProperties(SessionServer sessionServer) {
    ConfigAPI configAPI = new ConfigAPI("live/" + sessionServer.getGroupName() + "/" + sessionServer.getSubGroupName() + "/" + sessionServer.getServerName(), "server.properties", "=");
    configAPI.delete();
    configAPI.createFile();
    configAPI.set("server-port", sessionServer.getPort());
    configAPI.set("announce-player-achievements", false);
    configAPI.set("max-players", sessionServer.getSlots());
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
    configAPI.set("server-name", sessionServer.getServerName());
  }

  public void setCloudConfig(SessionServer sessionServer) {
    IniFile iniFile = new IniFile("live/" + sessionServer.getGroupName() + "/" + sessionServer.getSubGroupName() + "/" + sessionServer.getServerName() + "/cloud.ini");
    if (iniFile.isEmpty()) {
      iniFile.setProperty("serverName", sessionServer.getServerName());
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

  public String getPath(SessionServer sessionServer) {
    return livePath;
  }
}
