package de.leantwi.cloudsystem.proxy.server;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import de.leantwi.cloudsystem.proxy.database.RedisConnector;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class SessionServer {
    private static final int DATABASE_ID = 7;
    private final ProxyConnector proxyConnector = ProxyConnector.getInstance();
    private final RedisConnector redisConnector = this.proxyConnector.getRedisConnector();
    //
    private String hostName, serverName, wrapperID, groupName, subGroupName;
    private int port, onlinePlayers, memory, slots;
    private boolean startServer;
    private List<UUID> onlinePlayersList = new ArrayList<>();

    public void fetch(String serverName) {
        try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
            jedis.select(DATABASE_ID);
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
            this.memory = Integer.parseInt(map.get("weightClass"));
            this.onlinePlayers = Integer.parseInt(map.get("onlinePlayers"));
            this.slots = Integer.parseInt(map.get("slots"));
        }
    }

    public void deleteSession(String serverName) {

        try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            if (jedis.exists("cloud:sessions:server:" + serverName.toLowerCase())) {
                jedis.del("cloud:sessions:server:" + serverName.toLowerCase());
            }

        }
    }

    public void updateOnlinePlayersInRedis() {
        try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            jedis.hset("cloud:sessions:servers:" + this.serverName.toLowerCase(), "onlinePlayers", String.valueOf(this.onlinePlayersList.size()));
        }
    }

    public Integer getID() {
        return Integer.valueOf(this.serverName.split("-")[1]);
    }


}
