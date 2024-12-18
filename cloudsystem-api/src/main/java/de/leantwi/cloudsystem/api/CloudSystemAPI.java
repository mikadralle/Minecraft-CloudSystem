package de.leantwi.cloudsystem.api;

import com.mongodb.client.MongoClient;
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
import redis.clients.jedis.JedisPool;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CloudSystemAPI {

    MongoDBData getMongoDBData();

    RedisData getRedisData();

    NatsData getNatsData();

    EventHandlerAPI getEventHandler();

    NatsConnectorAPI getNatsConnector();

    RedisConnectorAPI getRedisConnectorAPI();

    MongoDBConnectorAPI getMongoDBConnectorAPI();

    JedisPool getRedisPool();

    MongoClient getMongoDBClient();

    GameServerData getGameServerByServerName(String serverName);

    List<GameServerData> getAllGameServerBySubGroupName(String subGroupName);

    List<GameServerData> getAllGameServerByGroupName(String groupName);

    List<GameServerData> getAllGameServers();

    void refreshGroups();

    void updateGameServer(GameServerData gameServerData);

    void deleteGameServer(GameServerData gameServerData);

    List<SubGroupDB> getAllSubGroups();

    List<SubGroupDB> getAllSubGroupByGroupName(String groupName);

    Collection<GroupDB> getAllGroups();


    GroupDB getGroupByName(String groupName);

    Optional<SubGroupDB> getSubGroupByName(String subGroupName);


    boolean existsGameServerByServerName(String serverName);

    void addNewSubGroupToGroup(String groupName, String subGroupName);

    void createGroup(String groupName);

    List<CloudPlayerAPI> getCloudPlayersByServerName(String serverName);

    List<CloudPlayerAPI> getAllCloudPlayers();


    void updateCloudPlayer(CloudPlayerAPI cloudPlayerAPI);

    void deleteCloudPlayer(CloudPlayerAPI cloudPlayerAPI);

    void updateCloudProxy(CloudProxyAPI cloudProxyAPI);

    void deleteCloudProxy(CloudProxyAPI cloudProxyAPI);

    GameServerData getGameServerByUniqueID(UUID uniqueID);

    String getLibrariesPathName();

}
