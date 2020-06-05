package eu.unyfy.master.handler.wrapper;

import eu.unyfy.master.Master;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrapperHandler {

  private final Map<String, WrapperServer> wrapperServerMap = new HashMap<>();

  public String verifyWrapper(String hostname, WrapperType wrapperType, int weightClass, int priority) {

    String wrapperID = "wrapper-" + getID();

    WrapperServer wrapperServer = new WrapperServer();
    wrapperServer.fetch(wrapperID, hostname, wrapperType, weightClass, priority);
    this.wrapperServerMap.put(wrapperID, wrapperServer);
    return wrapperID;

  }

  public void logoutWrapper(String wrapperName) {

    this.wrapperServerMap.remove(wrapperName);
    Master.getInstance().getConsole().sendMessage("The Wrapper " + wrapperName + " has been disconnected from master server!");

  }

  public WrapperServer getWrapperServer(String wrapperName) {
    return this.wrapperServerMap.get(wrapperName);
  }


  public String getRandomWrapper() {

    List<Integer> list = new ArrayList<>();
    this.wrapperServerMap.values().forEach(wrapperServer -> list.add(wrapperServer.getServerSessionList().size()));
    Collections.sort(list);
    return this.wrapperServerMap.keySet().stream().filter(wrapperName -> this.wrapperServerMap.get(wrapperName).getServerSessionList().size() == list.get(0)).findFirst().get();

  }

  public boolean isWrapperOnline() {
    return !this.wrapperServerMap.isEmpty();
  }

  public int getID() {

    for (int i = 1; i < 999; i++) {
      if (!this.wrapperServerMap.containsKey("wrapper-" + i)) {
        return i;
      }
    }
    return this.wrapperServerMap.keySet().size() + 1;
  }
}
