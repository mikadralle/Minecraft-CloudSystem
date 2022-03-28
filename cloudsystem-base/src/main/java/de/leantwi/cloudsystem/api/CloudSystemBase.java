package de.leantwi.cloudsystem.api;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.database.NatsConnectorAPI;
import de.leantwi.cloudsystem.api.database.RedisConnectorAPI;
import de.leantwi.cloudsystem.api.database.mongodb.MongoDBConnectorAPI;
import de.leantwi.cloudsystem.api.event.EventHandlerAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.groups.GroupDB;
import de.leantwi.cloudsystem.api.gameserver.groups.SubGroupDB;
import de.leantwi.cloudsystem.group.GroupHandler;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.stream.Collectors;

public class CloudSystemBase implements CloudSystemAPI {

    private MongoDBConnectorAPI mongoDBConnectorAPI;
    private NatsConnectorAPI natsConnectorAPI;
    private RedisConnectorAPI redisConnectorAPI;
    private final static int DATABASE_ID = 7;
    private final Gson gson = new Gson();
    private final static String REDIS_CLOUD_SERVER_PATH = "cloud:server";

    private GroupHandler groupHandler;

    public CloudSystemBase(NatsConnectorAPI natsConnectorAPI, RedisConnectorAPI redisConnectorAPI, MongoDBConnectorAPI mongoDBConnectorAPI) {
        this.natsConnectorAPI = natsConnectorAPI;
        this.redisConnectorAPI = redisConnectorAPI;
        this.mongoDBConnectorAPI = mongoDBConnectorAPI;

        this.natsConnectorAPI.connect();
        this.redisConnectorAPI.connect();
        this.mongoDBConnectorAPI.connect();

        this.groupHandler = new GroupHandler(this.mongoDBConnectorAPI);
        this.groupHandler.fetch();


    }

    @Override
    public EventHandlerAPI getEventHandler() {
        return CloudSystem.getEventAPI();
    }

    @Override
    public NatsConnectorAPI getNatsConnector() {
        return this.natsConnectorAPI;
    }

    @Override
    public RedisConnectorAPI getRedisConnectorAPI() {
        return this.redisConnectorAPI;
    }

    @Override
    public MongoDBConnectorAPI getMongoDBConnectorAPI() {
        return this.mongoDBConnectorAPI;
    }

    @Override
    public JedisPool getRedisPool() {
        return redisConnectorAPI.getJedisPool();
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

        List<SubGroupDB> allList = new ArrayList<>();

        for (GroupDB groupDB : this.groupHandler.getGroups().values()) {
            allList.addAll(groupDB.getSubGroupDBList());
        }

        return allList;
    }

    @Override
    public List<SubGroupDB> getAllSubGroupByGroupName(String groupName) {
        return this.groupHandler.getGroups().get(groupName.toLowerCase()).getSubGroupDBList();

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

    @Override
    public void createGroup(String groupName, String subGroupName) {
        this.groupHandler.createGroup(groupName, subGroupName);
    }

}
