package eu.unyfy.wrapper.core;

import eu.unyfy.wrapper.WrapperBootstrap;
import eu.unyfy.wrapper.core.server.SessionServer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;

public class WrapperCore {

  public final WrapperBootstrap wrapper = WrapperBootstrap.getInstance();
  public final ConcurrentHashMap<String, SessionServer> sessionServerMap = new ConcurrentHashMap<>();
  //
  private final FolderUtils folderUtils = this.wrapper.getFolderUtils();
  @Getter
  private final Map<String, SessionServer> onlineSession = new HashMap<>();
  private Process process;

  public void startServer() {

    if (this.sessionServerMap.isEmpty()) {
      return;
    }
    List<String> list = new ArrayList<>(this.sessionServerMap.keySet());
    start(this.sessionServerMap.get(list.get(0)));
    this.sessionServerMap.remove(list.get(0));

  }

  private void start(SessionServer sessionServer) {

    try {

      this.folderUtils.createTemp(sessionServer);
      Thread.sleep(500);

      this.process = new ProcessBuilder("screen", "-AmdS", sessionServer.getServerName().toLowerCase(), "java", "-Xms" + "512" + "M", "-Xmx" + "512" + "M", "-jar", "spigot.jar")
          .directory(new File("live/" + sessionServer.getGroupName() + "/" + sessionServer.getSubGroupName() + "/" + sessionServer.getServerName() + "/")).inheritIO().start();
      WrapperBootstrap.getInstance().getLogger().info("§aServer §e" + sessionServer.getServerName() + "§a will be started.");


    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void addWrapperList(String serverName) {
    SessionServer sessionServer = new SessionServer();
    sessionServer.fetch(serverName);
    this.sessionServerMap.put(serverName, sessionServer);
    this.onlineSession.put(serverName, sessionServer);
  }


  public boolean existsServer(String serverName) {
    return this.onlineSession.containsKey(serverName);
  }


}
