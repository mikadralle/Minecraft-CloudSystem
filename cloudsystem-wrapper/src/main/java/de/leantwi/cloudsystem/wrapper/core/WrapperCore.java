package de.leantwi.cloudsystem.wrapper.core;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;

public class WrapperCore {

  private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
  public final WrapperBootstrap wrapper = WrapperBootstrap.getInstance();
  public Queue<GameServerData> gameServerDataQueue = new LinkedList<>();
  //
  private final FolderUtils folderUtils = this.wrapper.getFolderUtils();
  @Getter
  private Process process;

  public void startServer() {

    if(this.gameServerDataQueue.isEmpty()) {
      return;
    }

    this.start(gameServerDataQueue.poll());
  }

  private void start(GameServerData gameServerData) {

    try {

      this.folderUtils.createTemp(gameServerData);
      Thread.sleep(500);

      final String serverName = gameServerData.getServerName();
      this.process = new ProcessBuilder("screen", "-AmdS", serverName.toLowerCase(), "java", "-Xms" + "512" + "M", "-Xmx" + "512" + "M", "-jar", "spigot.jar")
          .directory(new File("live/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + serverName + "/")).inheritIO().start();
      WrapperBootstrap.getInstance().getLogger().info("§aServer §e" + serverName + "§a will be started.");


    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void addWrapperList(String serverName) {
    this.gameServerDataQueue.add(this.cloudSystemAPI.getGameServerByServerName(serverName));
  }




}
