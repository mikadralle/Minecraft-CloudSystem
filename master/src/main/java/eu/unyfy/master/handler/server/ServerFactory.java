package eu.unyfy.master.handler.server;

import eu.unyfy.master.Master;
import eu.unyfy.master.database.group.ServerDB;
import eu.unyfy.master.database.group.SubGroupDB;
import eu.unyfy.master.database.redis.RedisConnector;
import eu.unyfy.master.handler.core.Core;
import eu.unyfy.master.handler.wrapper.WrapperHandler;
import redis.clients.jedis.Jedis;


public class ServerFactory {

  private final Master master = Master.getInstance();
  private final Core core = this.master.getCore();
  private final WrapperHandler wrapperHandler = this.core.getWrapperHandler();
  private final RedisConnector redisConnector = this.master.getRedisConnector();

  private SubGroupDB subGroupDB;
  private ServerDB serverDB;


  public void createServer(String subGroupName) {

    String serverName = subGroupName + "-" + getID(subGroupName);
    String wrapperName = this.wrapperHandler.getRandomWrapper();
    int port = this.wrapperHandler.getWrapperServer(wrapperName).getPort();

    this.subGroupDB = this.core.getSubGroup(subGroupName);
    this.serverDB = subGroupDB.getServerDB();
    SessionServer sessionServer = new SessionServer();
    sessionServer.createSession(this.subGroupDB.getGroupDB(), this.subGroupDB, serverName, this.wrapperHandler.getWrapperServer(wrapperName).getHostName(), wrapperName, this.serverDB.getMemory(), port, this.serverDB.getMaxPlayer());

    this.wrapperHandler.getWrapperServer(wrapperName).addServer(sessionServer);

    //send packet to wrapper, that create sessionServer
    this.master.getNatsConnector().sendMessage("cloud", "sessionServer#create#" + wrapperName + "#" + serverName);
    // put information in a core
    this.core.getCurrentSessionServer().put(serverName, sessionServer);
    this.subGroupDB.getStartSessionServerList().add(sessionServer);
    Master.getInstance().getConsole().sendMessage("the server " + serverName + " will be started");
  }

  public void deleteServer(SessionServer sessionServer) {
    this.wrapperHandler.getWrapperServer(sessionServer.getWrapperID()).removeServer(sessionServer);
    sessionServer.getSubGroupDB().getSessionServerList().remove(sessionServer);
    this.core.getCurrentSessionServer().remove(sessionServer.getServerName());
    sessionServer.deleteSession();
  }

  public int getID(String subGroupName) {

    for (int i = 1; i < 999; i++) {
      if (!this.core.getCurrentSessionServer().containsKey(subGroupName.toLowerCase() + "-" + i)) {
        return i;
      }
    }
    return this.core.getCurrentSubGroupServerOnlineSize(subGroupName) + 1;
  }

  public void addOnlineList(String serverName) {
    try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
      jedis.set("cloud:sessions:serverlist:" + serverName, serverName);
    }
  }

  public void removeOnlineList(String serverName) {
    try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
      jedis.del("cloud:sessions:serverlist:" + serverName, serverName);
    }
  }

}
