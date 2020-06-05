package eu.unyfy.wrapper.core.server;

import eu.unyfy.wrapper.Wrapper;
import eu.unyfy.wrapper.database.redis.RedisConnector;
import java.util.Map;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

@Getter
public class SessionServer {

  private final Wrapper wrapper = Wrapper.getInstance();
  private final RedisConnector redisConnector = this.wrapper.getRedisConnector();
  //
  private String hostName, serverName, wrapperID, groupName, subGroupName;
  private int port, onlinePlayers, memory, slots;

  public void fetch(String serverName) {
    try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
      Pipeline pipeline = jedis.pipelined();

      Response<Map<String, String>> mapResponse = pipeline.hgetAll("cloud:sessions:servers:" + serverName.toLowerCase());
      pipeline.sync();
      Map<String, String> map = mapResponse.get();

      this.hostName = map.get("hostName");
      this.serverName = map.get("serverID");
      this.groupName = map.get("groupName");
      this.subGroupName = map.get("subGroupName");
      this.wrapperID = map.get("wrapperID");
      this.port = Integer.parseInt(map.get("port"));
      this.memory = Integer.parseInt(map.get("memory"));
      this.onlinePlayers = Integer.parseInt(map.get("onlinePlayers"));
      this.slots = Integer.parseInt(map.get("slots"));
      //this.gameState = GametState.valueOf(map.get("gameState"));

    }
  }

  public void deleteSession(String serverName) {

    try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {

      if (jedis.exists("cloud:sessions:server:" + serverName.toLowerCase())) {
        jedis.del("cloud:sessions:server:" + serverName.toLowerCase());
      }

    }
  }

  public Integer getID() {
    return Integer.valueOf(this.serverName.split("-")[1]);
  }

}
