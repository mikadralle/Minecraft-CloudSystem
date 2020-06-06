package eu.unyfy.master.handler.wrapper;

import eu.unyfy.master.MasterBootstrap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class WrapperHandler {

  private final MasterBootstrap masterBootstrap = MasterBootstrap.getInstance();

  private final Map<String, WrapperServer> wrapperServerMap = new HashMap<>();

  public String verifyWrapper(String hostname, WrapperType wrapperType, int weightClass, int priority) {

    String wrapperID = null;
    if (priority == 100) {
      wrapperID = "wrapper-1";
    } else {
      wrapperID = "wrapper-" + getID();
    }

    WrapperServer wrapperServer = new WrapperServer();
    wrapperServer.fetch(wrapperID, hostname, wrapperType, weightClass, priority);
    this.wrapperServerMap.put(wrapperID, wrapperServer);
    return wrapperID;

  }

  public void logoutWrapper(String wrapperName) {
    this.wrapperServerMap.remove(wrapperName);
    this.masterBootstrap.sendMessage("The Wrapper " + wrapperName + " has been disconnected from master server!");

  }


  public String getRandomWrapper(String subGroupName, int weightClass) {

    if (getWrapperServer("wrapper-1").isFreeSlot(subGroupName)) {
      System.out.println("true");
      return "wrapper-1";
    }

    System.out.println("false");
    List<WrapperServer> wrapperServerList = new ArrayList<>();
    wrapperServerList.addAll(this.wrapperServerMap.values());
    wrapperServerList.remove(this.getWrapperServer("wrapper-1"));
    wrapperServerList.sort(Comparator.comparingInt(WrapperServer::getPriority));

    if (wrapperServerList.stream().anyMatch(wrapper -> wrapper.getWeightClass() > weightClass)) {
      return wrapperServerList.stream().filter(wrapper -> wrapper.getWeightClass() > weightClass).findAny().get().getWrapperID();
    }

    startNewWrapper();
    return null;

    /*
    List<Integer> list = new ArrayList<>();
    this.wrapperServerMap.values().forEach(wrapperServer -> list.add(wrapperServer.getServerSessionList().size()));
    Collections.sort(list);
    return this.wrapperServerMap.keySet().stream().filter(wrapperName -> this.wrapperServerMap.get(wrapperName).getServerSessionList().size() == list.get(0)).findFirst().get();
     */
  }

  private void startNewWrapper() {
    System.out.println("Start new Wrapper!");
  }

  public boolean isWrapperOnline() {
    return !this.wrapperServerMap.isEmpty();
  }

  public int getID() {

    for (int i = 2; i < 999; i++) {
      if (!this.wrapperServerMap.containsKey("wrapper-" + i)) {
        return i;
      }
    }
    return this.wrapperServerMap.keySet().size() + 1;
  }

  public WrapperServer getWrapperServer(String wrapperName) {
    return this.wrapperServerMap.get(wrapperName);
  }

}
