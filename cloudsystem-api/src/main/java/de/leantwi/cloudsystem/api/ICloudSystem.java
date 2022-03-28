package de.leantwi.cloudsystem.api;

import com.mongodb.MongoClient;
import de.leantwi.cloudsystem.api.database.INats;
import de.leantwi.cloudsystem.api.database.IRedis;
import de.leantwi.cloudsystem.api.event.IEventHandler;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.groups.GroupDB;
import de.leantwi.cloudsystem.api.gameserver.groups.SubGroupDB;
import redis.clients.jedis.JedisPool;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ICloudSystem {

    IEventHandler getEventHandler();

    INats getNatsConnector();
    JedisPool getRedisPool();
    MongoClient getMongoDBClient();


    GameServerData getGameServerByServerName(String serverName);

    List<GameServerData> getAllGameServerBySubGroupName(String subGroupName);

    List<GameServerData> getAllGameServerByGroupName(String groupName);

    List<GameServerData> getAllGameServer();

    void updateGameServer(GameServerData gameServerData);

    void deleteGameServer(GameServerData gameServerData);

    List<SubGroupDB> getAllSubGroups();
    List<SubGroupDB> getAllSubGroupByGroupName(String groupName);
    Collection<GroupDB> getAllGroups();
    GroupDB getGroupByName(String groupName);
    Optional<SubGroupDB> getSubGroupByName(String subGroupName);




}
