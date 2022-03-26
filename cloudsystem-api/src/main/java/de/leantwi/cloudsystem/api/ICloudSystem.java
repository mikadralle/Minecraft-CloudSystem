package de.leantwi.cloudsystem.api;

import de.leantwi.cloudsystem.api.database.INats;
import de.leantwi.cloudsystem.api.database.IRedis;
import de.leantwi.cloudsystem.api.event.IEventHandler;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import redis.clients.jedis.JedisPool;

import java.util.List;

public interface ICloudSystem {

    IEventHandler getEventHandler();

    INats getNatsConnector();
    JedisPool getRedisPool();

    GameServerData getGameServerByServerName(String serverName);

    List<GameServerData> getAllGameServerBySubGroupName(String subGroupName);

    List<GameServerData> getAllGameServerByGroupName(String groupName);

    List<GameServerData> getAllGameServer();


}
