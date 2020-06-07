package eu.unyfy.master.handler.server;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.database.group.GroupDB;
import eu.unyfy.master.database.group.SubGroupDB;
import eu.unyfy.master.database.redis.RedisConnector;
import eu.unyfy.master.handler.core.Core;
import eu.unyfy.master.handler.group.GametState;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

@Getter
public class SessionServer {

  private final MasterBootstrap master = MasterBootstrap.getInstance();
  private final Core core = this.master.getCore();
  private final RedisConnector redisConnector = this.master.getRedisConnector();
  //
  private String hostName, serverName, wrapperID;
  private int port, onlinePlayers, weightClass, slots;
  private GametState gameState;
  private SubGroupDB subGroupDB;
  private GroupDB groupDB;

  public void createSession(GroupDB groupDB, SubGroupDB subGroupDB, String serverName, String hostName, String wrapperID, int weightClass, int port, int slots) {

    try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {

      Pipeline pipeline = jedis.pipelined();
      Map<String, String> map = new HashMap<>();

      map.put("hostName", hostName);
      map.put("serverID", serverName);
      map.put("groupName", groupDB.getGroupName());
      map.put("subGroupName", subGroupDB.getSubGroupName());
      map.put("wrapperID", wrapperID);
      map.put("weightClass", String.valueOf(weightClass));
      map.put("port", String.valueOf(port));
      map.put("onlinePlayers", String.valueOf(0));
      map.put("slots", String.valueOf(slots));
      map.put("gameState", "LOBBY");

      pipeline.hset("cloud:sessions:servers:" + serverName.toLowerCase(), map);
      pipeline.sync();

      this.hostName = hostName;
      this.serverName = serverName;
      this.groupDB = groupDB;
      this.subGroupDB = subGroupDB;
      this.wrapperID = wrapperID;
      this.port = port;
      this.weightClass = weightClass;
      this.onlinePlayers = 0;
      this.slots = slots;
      this.gameState = GametState.LOBBY;


    }

  }

  public void fetch() {
    try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
      Pipeline pipeline = jedis.pipelined();

      Response<Map<String, String>> mapResponse = pipeline.hgetAll("cloud:sessions:servers:" + this.serverName.toLowerCase());
      pipeline.sync();
      Map<String, String> map = mapResponse.get();

      this.hostName = map.get("hostName");
      this.serverName = map.get("serverID");
      this.groupDB = this.core.getGroupDBByName(map.get("groupName"));
      this.subGroupDB = this.core.getSubGroup(map.get("subGroupName"));
      this.wrapperID = map.get("wrapperID");
      this.port = Integer.parseInt(map.get("port"));
      this.weightClass = Integer.parseInt(map.get("weightClass"));
      this.onlinePlayers = Integer.parseInt(map.get("onlinePlayers"));
      this.slots = Integer.parseInt(map.get("slots"));
      //this.gameState = GametState.valueOf(map.get("gameState"));

    }
  }

  public void deleteSession() {

    try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {

      if (jedis.exists("cloud:sessions:server:" + this.serverName.toLowerCase())) {
        jedis.del("cloud:sessions:server:" + this.serverName.toLowerCase());
      }

    }
  }

  public Integer getID() {
    return Integer.valueOf(this.serverName.split("-")[1]);
  }

}
