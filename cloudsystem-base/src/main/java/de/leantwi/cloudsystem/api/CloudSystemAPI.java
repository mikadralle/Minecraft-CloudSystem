package de.leantwi.cloudsystem.api;

import com.google.gson.Gson;
import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.database.INats;
import de.leantwi.cloudsystem.api.database.IRedis;
import de.leantwi.cloudsystem.api.event.IEventHandler;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CloudSystemAPI implements ICloudSystem {

    private INats iNats;
    private IRedis iRedis;
    private final Gson gson = new Gson();
    private final static String REDIS_CLOUD_SERVER_PATH = "cloud:server";

    public CloudSystemAPI(INats iNats, IRedis iRedis) {
        this.iNats = iNats;
        this.iRedis = iRedis;

        this.iNats.connect();
        this.iRedis.connect();
    }

    @Override
    public IEventHandler getEventHandler() {
        return CloudSystem.getEventAPI();
    }

    @Override
    public INats getNatsConnector() {
        return this.iNats;
    }

    @Override
    public JedisPool getRedisPool() {
        return iRedis.getJedisPool();
    }

    @Override
    public GameServerData getGameServerByServerName(String serverName) {

        GameServerData gameServerData;
        try (Jedis jedis = this.getRedisPool().getResource()) {
            gameServerData = gson.fromJson(jedis.hget(REDIS_CLOUD_SERVER_PATH, serverName), GameServerData.class);
        }

        return gameServerData;
    }

    @Override
    public List<GameServerData> getAllGameServerBySubGroupName(String subGroupName) {

        try (Jedis jedis = this.getRedisPool().getResource()) {

        }
        return null;
    }

    @Override
    public List<GameServerData> getAllGameServerByGroupName(String groupName) {
        return null;
    }

    @Override
    public List<GameServerData> getAllGameServer() {
        List<GameServerData> list = new ArrayList<>();
        try (Jedis jedis = this.getRedisPool().getResource()) {
            Map<String, String> map = jedis.hgetAll(REDIS_CLOUD_SERVER_PATH);

            map.values().forEach(f -> {
                list.add(gson.fromJson(f, GameServerData.class));
            });
        }
        return list;
    }

}
