package de.leantwi.cloudsystem.api;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.database.INats;
import de.leantwi.cloudsystem.api.database.IRedis;
import de.leantwi.cloudsystem.api.database.mongodb.IMongoDB;
import de.leantwi.cloudsystem.api.event.IEventHandler;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.groups.GroupDB;
import de.leantwi.cloudsystem.api.gameserver.groups.SubGroupDB;
import de.leantwi.cloudsystem.group.GroupHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.stream.Collectors;

public class CloudSystemAPI implements ICloudSystem {

    private IMongoDB iMongoDB;
    private INats iNats;
    private IRedis iRedis;
    private final static int DATABASE_ID = 7;
    private final Gson gson = new Gson();
    private final static String REDIS_CLOUD_SERVER_PATH = "cloud:server";

    private GroupHandler groupHandler;

    public CloudSystemAPI(INats iNats, IRedis iRedis, IMongoDB iMongoDB) {
        this.iNats = iNats;
        this.iRedis = iRedis;
        this.iMongoDB = iMongoDB;

        this.iNats.connect();
        this.iRedis.connect();
        this.iMongoDB.connect();

        this.groupHandler = new GroupHandler(this.iMongoDB);
        this.groupHandler.fetch();


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
    public MongoClient getMongoDBClient() {
        return null;
    }

    @Override
    public GameServerData getGameServerByServerName(String serverName) {

        GameServerData gameServerData;
        try (Jedis jedis = this.getRedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            gameServerData = gson.fromJson(jedis.hget(REDIS_CLOUD_SERVER_PATH, serverName), GameServerData.class);
        }

        return gameServerData;
    }

    @Override
    public List<GameServerData> getAllGameServerBySubGroupName(String subGroupName) {
        return getAllGameServer().stream()
                .filter(gameServerData -> gameServerData.getSubGroupDB().equalsIgnoreCase(subGroupName))
                .collect(Collectors.toList());
    }

    @Override
    public List<GameServerData> getAllGameServerByGroupName(String groupName) {
        return getAllGameServer().stream()
                .filter(gameServerData -> gameServerData.getGroupDB().equalsIgnoreCase(groupName))
                .collect(Collectors.toList());

    }

    @Override
    public List<GameServerData> getAllGameServer() {
        List<GameServerData> list = new ArrayList<>();
        try (Jedis jedis = this.getRedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            Map<String, String> map = jedis.hgetAll(REDIS_CLOUD_SERVER_PATH);

            map.values().forEach(f -> {
                list.add(gson.fromJson(f, GameServerData.class));
            });
        }
        return list;
    }

    @Override
    public void updateGameServer(GameServerData gameServerData) {

        try (Jedis jedis = this.getRedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            jedis.hset(REDIS_CLOUD_SERVER_PATH, gameServerData.getServerName(), this.gson.toJson(gameServerData));
        }

    }

    @Override
    public void deleteGameServer(GameServerData gameServerData) {
        try (Jedis jedis = this.getRedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            jedis.hset(REDIS_CLOUD_SERVER_PATH, gameServerData.getServerName(), this.gson.toJson(gameServerData));
        }


    }

    @Override
    public List<SubGroupDB> getAllSubGroups() {
        return null;
    }

    @Override
    public List<SubGroupDB> getAllSubGroupByGroupName(String groupName) {
        return null;
    }

    @Override
    public Collection<GroupDB> getAllGroups() {
        return this.groupHandler.getGroups().values();
    }

    @Override
    public GroupDB getGroupByName(String groupName) {
        return this.groupHandler.getGroups().get(groupName);
    }

    @Override
    public Optional<SubGroupDB> getSubGroupByName(String subGroupName) {


        for (GroupDB groupDB : this.groupHandler.getGroups().values()) {

            Optional<SubGroupDB> subGroupDB = groupDB.getSubGroupDBList().stream().filter(f -> f.getSubGroupName().equalsIgnoreCase(subGroupName)).findFirst();
            if (subGroupDB.isPresent()) {
                return subGroupDB;
            }
        }
        return null;
    }

}
