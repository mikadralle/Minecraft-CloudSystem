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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.stream.Collectors;

public class CloudSystemBase implements CloudSystemAPI {

    private final MongoDBConnectorAPI mongoDBConnectorAPI;
    private final NatsConnectorAPI natsConnectorAPI;
    private final RedisConnectorAPI redisConnectorAPI;
    public final int DATABASE_ID = 7;
    private final Gson gson = new Gson();
    public final String REDIS_CLOUD_SERVER_PATH = "cloud:server";
    public final String REDIS_CLOUD_PLAYERS_PATH = "cloud:players:";

    private final GroupHandler groupHandler;

    private final CloudPlayerAPI cloudPlayer;

    public CloudSystemBase(NatsConnectorAPI natsConnectorAPI, RedisConnectorAPI redisConnectorAPI, MongoDBConnectorAPI mongoDBConnectorAPI) {
        this.natsConnectorAPI = natsConnectorAPI;
        this.redisConnectorAPI = redisConnectorAPI;
        this.mongoDBConnectorAPI = mongoDBConnectorAPI;

        this.natsConnectorAPI.connect();
        this.redisConnectorAPI.connect();
        this.mongoDBConnectorAPI.connect();

        this.groupHandler = new GroupHandler(this.mongoDBConnectorAPI);
        this.groupHandler.fetch();


        this.cloudPlayer = new CloudPlayer();


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
    public void refreshGroups() {
        this.groupHandler.fetch();
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
            jedis.hdel(REDIS_CLOUD_SERVER_PATH, gameServerData.getServerName());
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
        return Optional.empty();
    }

    @Override
    public boolean existsGameServerByServerName(String serverName) {

        try (Jedis jedis = this.getRedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            return jedis.hexists(REDIS_CLOUD_SERVER_PATH, serverName);
        }
    }

    @Override
    public void createGroup(String groupName) {

        this.groupHandler.createGroup(groupName);

    }

    @Override
    public void addNewSubGroupToGroup(String groupName, String subGroupName) {

        this.groupHandler.addSubGroupToGroup(groupName, subGroupName);
    }

    @Override
    public CloudPlayerAPI getCloudPlayerByName(String playerName) {


        return null;
    }

    @Override
    public CloudPlayerAPI getCloudPlayerByUUID(UUID uuid) {


        try (Jedis jedis = this.getRedisPool().getResource()) {
            jedis.select(DATABASE_ID);

            if (jedis.exists(REDIS_CLOUD_PLAYERS_PATH + uuid)) {
                return gson.fromJson(jedis.hget(REDIS_CLOUD_PLAYERS_PATH + uuid, "json"), CloudPlayer.class);
            } else {


                CloudPlayer cloudPlayer = new CloudPlayer();

                cloudPlayer.setUniqueID(uuid);
                cloudPlayer.setLastJoin(System.currentTimeMillis());
                jedis.hset(REDIS_CLOUD_PLAYERS_PATH + uuid, "json", gson.toJson(cloudPlayer));
                return cloudPlayer;
            }

        }
    }


    @Override
    public List<CloudPlayerAPI> getCloudPlayersByServerName(String serverName) {
        return null;
    }

    @Override
    public boolean existsCloudPlayer(String playerName) {

        return false;
    }

    @Override
    public boolean existsCloudPlayer(UUID uniqueID) {

        try (Jedis jedis = this.getRedisPool().getResource()) {

            jedis.select(DATABASE_ID);

            return jedis.exists(REDIS_CLOUD_PLAYERS_PATH + uniqueID);


        }
    }

    @Override
    public void updateCloudPlayer(CloudPlayerAPI cloudPlayer) {

        try (Jedis jedis = this.getRedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            jedis.hset(REDIS_CLOUD_PLAYERS_PATH + cloudPlayer.getUniqueID(), "json", this.gson.toJson(cloudPlayer));
        }
    }

    @Override
    public void deleteCloudPlayer(CloudPlayerAPI cloudPlayerAPI) {
        try (Jedis jedis = this.getRedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            jedis.hdel(REDIS_CLOUD_PLAYERS_PATH + cloudPlayerAPI.getUniqueID(), "json");
        }


    }

}
