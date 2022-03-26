package de.leantwi.cloudsystem.api.gameserver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
public class GameServerData {

    private String hostName, serverName, wrapperID, subGroupDB, groupDB;
    private int port, onlinePlayers, weightClass, maxOnlinePlayers;
    private GameState gameState;
/*
    public void fetch(JedisPool jedisPool) {
        try (Jedis jedis = jedisPool.getResource()) {
            Pipeline pipeline = jedis.pipelined();

            Response<Map<String, String>> mapResponse = pipeline.hgetAll("cloud:sessions:servers:" + this.serverName.toLowerCase());
            pipeline.sync();
            Map<String, String> map = mapResponse.get();

            this.hostName = map.get("hostName");
            this.serverName = map.get("serverID");
            this.groupDB = map.get("groupName");
            this.subGroupDB = map.get("subGroupName");
            this.wrapperID = map.get("wrapperID");
            this.port = Integer.parseInt(map.get("port"));
            this.weightClass = Integer.parseInt(map.get("weightClass"));
            this.onlinePlayers = Integer.parseInt(map.get("onlinePlayers"));
            this.maxOnlinePlayers = Integer.parseInt(map.get("maxOnlinePlayers"));
            this.gameState = GameState.valueOf(map.get("gameState"));
        }
    }

    public void updateSession(JedisPool jedisPool) {

        try (Jedis jedis = jedisPool.getResource()) {

            Pipeline pipeline = jedis.pipelined();
            Map<String, String> map = new HashMap<>();

            map.put("hostName", this.hostName);
            map.put("serverID", this.serverName);
            map.put("groupName", this.groupDB);
            map.put("subGroupName", this.subGroupDB);
            map.put("wrapperID", this.wrapperID);
            map.put("weightClass", String.valueOf(this.weightClass));
            map.put("port", String.valueOf(this.port));
            map.put("onlinePlayers", String.valueOf(0));
            map.put("maxOnlinePlayers", String.valueOf(this.maxOnlinePlayers));
            map.put("gameState", this.gameState.toString());

            pipeline.hset("cloud:sessions:servers:" + serverName.toLowerCase(), map);
            pipeline.sync();
        }
    }

    public void deleteSession(JedisPool jedisPool) {

        try (Jedis jedis = jedisPool.getResource()) {

            if (jedis.exists("cloud:sessions:server:" + this.serverName.toLowerCase())) {
                jedis.del("cloud:sessions:server:" + this.serverName.toLowerCase());
            }

        }
    }

 */


}
