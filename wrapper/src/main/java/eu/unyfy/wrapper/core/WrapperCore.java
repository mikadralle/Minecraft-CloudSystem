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

    System.out.println("List: " + sessionServerMap.keySet().toString());
    if (this.sessionServerMap.isEmpty()) {
      System.out.println("I am empty bro, please feed me!");
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

      this.process = new ProcessBuilder("screen", "-AmdS", sessionServer.getServerName().toLowerCase(), "java", "-Xms" + sessionServer.getMemory() + "M", "-Xmx" + sessionServer.getMemory() + "M", "-jar", "spigot.jar")
          .directory(new File("live/" + sessionServer.getGroupName() + "/" + sessionServer.getSubGroupName() + "/" + sessionServer.getServerName() + "/")).inheritIO().start();
      Cache.sendMessage("Server " + sessionServer.getServerName() + " will be started.");

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void addWrapperList(String serverName) {
    System.out.println("I have been added to a list." + serverName);
    SessionServer sessionServer = new SessionServer();
    sessionServer.fetch(serverName);

    this.sessionServerMap.put(serverName, sessionServer);

    System.out.println("List add: " + sessionServerMap.keySet().toString());

  }


}
