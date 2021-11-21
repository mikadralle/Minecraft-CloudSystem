package de.leantwi.cloudsystem.master.handler.core;

import de.leantwi.cloudsystem.master.handler.server.SessionServer;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.database.group.GroupDB;
import de.leantwi.cloudsystem.master.database.group.SubGroupDB;
import de.leantwi.cloudsystem.master.handler.service.ServerOnlineAmountService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;

@Getter
public class Core {

  //
  @Getter
  private final ExecutorService executorService = Executors.newCachedThreadPool();
  //
  private final MasterBootstrap master = MasterBootstrap.getInstance();
  // map's

  private final ConcurrentHashMap<String, SessionServer> currentSessionServer = new ConcurrentHashMap<>();//serverName,SessionServer
  private final List<GroupDB> groupsDBList = new ArrayList<>(); //GroupDB,SubGroup
  //  private final ConcurrentHashMap<GroupDB, String> groupDBStringMap = new ConcurrentHashMap<>();//GroupDB,SubGroupString
  private final ConcurrentHashMap<String, SubGroupDB> subGroupDBString = new ConcurrentHashMap<>();//SubGroupString,SubGroupDB

  // Core

  public void startService() {
    this.executorService.execute(new ServerOnlineAmountService());
  }

  public SessionServer getSessionServer(String serverName) {
    return this.currentSessionServer.get(serverName);
  }

  public GroupDB getGroupDBByName(String groupDBName) {
    return this.master.getGroupHandler().getGroups().get(groupDBName);
  }

  public Integer getCurrentSubGroupServerOnlineSize(String subGroupName) {

    if (this.getSubGroup(subGroupName).getAllServerList().isEmpty()) {
      return 0;
    }

    return this.getSubGroup(subGroupName).getAllServerList().size();
  }

  public GroupDB getGroupDBFromSubGroup(String subGroupName) {
    return this.groupsDBList.stream().filter(groupDB -> groupDB.getSubGroupDBList().contains(this.subGroupDBString.get(subGroupName))).findFirst().get();
  }

  public SubGroupDB getSubGroup(String subGroupName) {

    return getGroupDBFromSubGroup(subGroupName).getSubGroupDBList().stream().filter(subGroupDB ->
        subGroupDB.getSubGroupName().equalsIgnoreCase(subGroupName)).findFirst().orElse(null);
  }
}
