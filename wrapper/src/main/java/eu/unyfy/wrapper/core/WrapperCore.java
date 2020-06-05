package eu.unyfy.wrapper.core;

import eu.unyfy.wrapper.Wrapper;
import eu.unyfy.wrapper.core.server.SessionServer;
import eu.unyfy.wrapper.utils.Cache;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WrapperCore {

  public final Wrapper wrapper = Wrapper.getInstance();
  public final ConcurrentHashMap<String, SessionServer> sessionServerMap = new ConcurrentHashMap<>();
  //
  //
  //
  //public final HashMap<ServerCore, Long> startPhase = new HashMap<>();
  private final FolderUtils folderUtils = this.wrapper.getFolderUtils();
  private Process process;

  //private final List<Integer> badPortList = new ArrayList<>();

  public void startServer() {

    if (this.sessionServerMap.isEmpty()) {
      return;
    }
    List<String> list = new ArrayList<>();
    list.addAll(this.sessionServerMap.keySet());
    start(this.sessionServerMap.get(list.get(0)));
    this.sessionServerMap.remove(list.get(0));

  }

  private void start(SessionServer sessionServer) {

    try {

      this.folderUtils.createTemp(sessionServer);
      Thread.sleep(500);

      this.process = new ProcessBuilder("screen", "-AmdS", sessionServer.getServerName().toLowerCase(), "java", "-Xms" + "512" + "M", "-Xmx" + "512" + "M", "-jar", "spigot.jar")
          .directory(new File("live/" + sessionServer.getGroupName() + "/" + sessionServer.getSubGroupName() + "/" + sessionServer.getServerName() + "/")).inheritIO().start();
      Cache.sendMessage("Server " + sessionServer.getServerName() + " will be started.");

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void addWrapperList(String serverName) {
    SessionServer sessionServer = new SessionServer();
    sessionServer.fetch(serverName);

    this.sessionServerMap.put(serverName, sessionServer);


  }


}
