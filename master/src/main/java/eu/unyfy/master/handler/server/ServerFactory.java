package eu.unyfy.master.handler.server;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.database.group.ServerDB;
import eu.unyfy.master.database.group.SubGroupDB;
import redis.clients.jedis.Jedis;


public class ServerFactory {

  private final MasterBootstrap master = MasterBootstrap.getInstance();

  private SubGroupDB subGroupDB;
  private ServerDB serverDB;


  public void createServer(String subGroupName) {

    String serverName = subGroupName + "-" + getID(subGroupName);
    this.subGroupDB = master.getCore().getSubGroup(subGroupName);
    this.serverDB = subGroupDB.getServerDB();

    String wrapperName = this.master.getWrapperHandler().getRandomWrapper(subGroupName, serverDB.getWeightClass());
    System.out.println("ServerName: " + serverName);
    System.out.println("Wrapper-Name: " + wrapperName);
    if (wrapperName == null) {
      System.out.println("Wrapper is null!");
      return;
    }
    int port = this.master.getWrapperHandler().getWrapperServer(wrapperName).getPort();

    SessionServer sessionServer = new SessionServer();
    sessionServer.createSession(this.subGroupDB.getGroupDB(), this.subGroupDB, serverName, this.master.getWrapperHandler().getWrapperServer(wrapperName).getHostName(), wrapperName, this.serverDB.getWeightClass(), port, this.serverDB.getMaxPlayer());

    this.master.getWrapperHandler().getWrapperServer(wrapperName).addServer(sessionServer);

    //send packet to wrapper, that create sessionServer
    this.master.getNatsConnector().sendMessage("cloud", "sessionServer#create#" + wrapperName + "#" + serverName);
    // put information in a core
    this.master.getCore().getCurrentSessionServer().put(serverName, sessionServer);
    this.subGroupDB.getStartSessionServerList().add(sessionServer);
    this.master.sendMessage("the server " + serverName + " will be started");
  }

  public void deleteServer(SessionServer sessionServer) {
    this.master.getWrapperHandler().getWrapperServer(sessionServer.getWrapperID()).removeServer(sessionServer);
    sessionServer.getSubGroupDB().getSessionServerList().remove(sessionServer);
    this.master.getCore().getCurrentSessionServer().remove(sessionServer.getServerName());
    sessionServer.deleteSession();
  }

  public int getID(String subGroupName) {

    for (int i = 1; i < 999; i++) {
      if (!this.master.getCore().getCurrentSessionServer().containsKey(subGroupName.toLowerCase() + "-" + i)) {
        return i;
      }
    }
    return this.master.getCore().getCurrentSubGroupServerOnlineSize(subGroupName) + 1;
  }

  public void addOnlineList(String serverName) {
    try (Jedis jedis = this.master.getMainDatabase().getRedisConnector().getJedisPool().getResource()) {
      jedis.set("cloud:sessions:serverlist:" + serverName, serverName);
    }
  }

  public void removeOnlineList(String serverName) {
    try (Jedis jedis = this.master.getMainDatabase().getRedisConnector().getJedisPool().getResource()) {
      jedis.del("cloud:sessions:serverlist:" + serverName, serverName);
    }
  }

}
