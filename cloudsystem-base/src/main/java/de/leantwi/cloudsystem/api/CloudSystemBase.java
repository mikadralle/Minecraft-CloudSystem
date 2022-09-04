package de.leantwi.cloudsystem.api;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.database.NatsConnectorAPI;
import de.leantwi.cloudsystem.api.database.RedisConnectorAPI;
import de.leantwi.cloudsystem.api.database.data.MongoDBData;
import de.leantwi.cloudsystem.api.database.data.NatsData;
import de.leantwi.cloudsystem.api.database.data.RedisData;
import de.leantwi.cloudsystem.api.database.mongodb.MongoDBConnectorAPI;
import de.leantwi.cloudsystem.api.event.EventHandlerAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.groups.GroupDB;
import de.leantwi.cloudsystem.api.gameserver.groups.SubGroupDB;
import de.leantwi.cloudsystem.database.MongoDBConnector;
import de.leantwi.cloudsystem.database.NatsConnector;
import de.leantwi.cloudsystem.database.RedisConnector;
import de.leantwi.cloudsystem.group.GroupHandler;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.stream.Collectors;

public class CloudSystemBase implements CloudSystemAPI {

    private final MongoDBConnectorAPI mongoDBConnectorAPI;
    private final NatsConnectorAPI natsConnectorAPI;
    private final RedisConnectorAPI redisConnectorAPI;
    public int DATABASE_ID;
    public final String REDIS_CLOUD_PLAYERS_NAME_PATH = "cloud:databases:names:";
    public final String REDIS_CLOUD_SERVER_PATH = "cloud:server";
    public final String REDIS_CLOUD_PLAYERS_PATH = "cloud:players:";
    public final String REDIS_CLOUD_PROXY_PATH = "cloud:proxy:";
    private final Gson gson = GsonHandler.getGson();

    @Getter
    private final GroupHandler groupHandler;

    private final NatsData natsData;
    private final MongoDBData mongoDBData;
    private final RedisData redisData;

    public CloudSystemBase(NatsData natsData, RedisData redisData, MongoDBData mongoDBData) {
        this.natsData = natsData;
        this.mongoDBData = mongoDBData;
        this.redisData = redisData;
        this.DATABASE_ID = redisData.getDatabaseID();
        this.natsConnectorAPI = new NatsConnector(natsData.getHostName(), natsData.getToken(), natsData.getPort());
        this.redisConnectorAPI = new RedisConnector(redisData.getHostName(), redisData.getPassword(), redisData.getPort(), redisData.getDatabaseID());
        this.mongoDBConnectorAPI = new MongoDBConnector(mongoDBData.getHostName(), mongoDBData.getAuthDB(), mongoDBData.getDefaultDB(), mongoDBData.getUserName(), mongoDBData.getPassword());

        this.natsConnectorAPI.connect();
        this.redisConnectorAPI.connect();
        this.mongoDBConnectorAPI.connect();

        this.groupHandler = new GroupHandler(this.mongoDBConnectorAPI);
        this.groupHandler.fetch();

    }


    @Override
    public MongoDBData getMongoDBData() {
        return this.mongoDBData;
    }

    @Override
    public RedisData getRedisData() {
        return this.redisData;
    }

    @Override
    public NatsData getNatsData() {
        return this.natsData;
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
        return getAllGameServers().stream()
                .filter(gameServerData -> gameServerData.getSubGroupDB().equalsIgnoreCase(subGroupName))
                .collect(Collectors.toList());
    }

    @Override
    public List<GameServerData> getAllGameServerByGroupName(String groupName) {
        return getAllGameServers().stream()
                .filter(gameServerData -> gameServerData.getGroupDB().equalsIgnoreCase(groupName))
                .collect(Collectors.toList());

    }

    @Override
    public List<GameServerData> getAllGameServers() {
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
    public List<CloudPlayerAPI> getCloudPlayersByServerName(String serverName) {
        return null;
    }

    @Override
    public List<CloudPlayerAPI> getAllCloudPlayers() {

        List<CloudPlayerAPI> list = new ArrayList<>();

        try (Jedis jedis = this.redisConnectorAPI.getJedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            jedis.keys(REDIS_CLOUD_PLAYERS_PATH + "*").forEach(key -> {
                String uuid = key.split(":")[2];
                list.add(this.gson.fromJson(jedis.hget(REDIS_CLOUD_PLAYERS_PATH + uuid, uuid), CloudPlayerAPI.class));
            });
        }

        return list;
    }

    @Override
    public void updateCloudPlayer(CloudPlayerAPI cloudPlayer) {
        try (Jedis jedis = this.getRedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            if (!jedis.exists(REDIS_CLOUD_PLAYERS_NAME_PATH + cloudPlayer.getPlayerName())) {
                jedis.hset(REDIS_CLOUD_PLAYERS_NAME_PATH + cloudPlayer.getPlayerName().toLowerCase(), "uuid", cloudPlayer.getUniqueID().toString());
            }
            jedis.hset(REDIS_CLOUD_PLAYERS_PATH + cloudPlayer.getUniqueID(), cloudPlayer.getUniqueID().toString(), gson.toJson(cloudPlayer, CloudPlayerAPI.class));
        }
    }

    @Override
    public void deleteCloudPlayer(CloudPlayerAPI cloudPlayerAPI) {
        try (Jedis jedis = this.getRedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            jedis.del(REDIS_CLOUD_PLAYERS_NAME_PATH + cloudPlayerAPI.getPlayerName().toLowerCase());
            jedis.hdel(REDIS_CLOUD_PLAYERS_PATH + cloudPlayerAPI.getUniqueID(), cloudPlayerAPI.getUniqueID().toString());

        }
    }

    @Override
    public void updateCloudProxy(CloudProxyAPI cloudProxyAPI) {
        try (Jedis jedis = this.redisConnectorAPI.getJedisPool().getResource()) {
            jedis.select(DATABASE_ID);
            jedis.hset(REDIS_CLOUD_PROXY_PATH + cloudProxyAPI.getProxyID(), "json", this.gson.toJson(cloudProxyAPI, CloudProxyAPI.class));
        }
    }

    @Override
    public void deleteCloudProxy(CloudProxyAPI cloudProxyAPI) {

    }

}
