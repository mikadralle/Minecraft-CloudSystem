package de.leantwi.cloudsystem.api;

import com.google.gson.Gson;
import de.leantwi.cloudsystem.CloudProxy;
import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.events.player.ConnectCloudPlayerToServerEvent;
import de.leantwi.cloudsystem.api.events.player.KickCloudPlayerEvent;
import de.leantwi.cloudsystem.api.events.player.SendMessageToCloudPlayerEvent;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class CloudPlayer extends CloudPlayerAPI {
    public static final String REDIS_CLOUD_PLAYERS_PATH = "cloud:players:";
    public static final String REDIS_CLOUD_PLAYERS_NAME_PATH = "cloud:databases:names:";
    private final static Gson gson = GsonHandler.getGson();
    private final int databaseID = CloudSystem.getAPI().getRedisData().getDatabaseID();

    public CloudPlayer(String playerName, String serverName, String proxyID, UUID uuid, long lastJoinTime) {
        super(playerName, serverName, proxyID, uuid, lastJoinTime);
    }

    public static CloudPlayer getCloudPlayer(UUID uuid) {

        CloudPlayer cloudPlayerAPI;
        try (Jedis jedis = CloudSystem.getAPI().getRedisPool().getResource()) {
            jedis.select(CloudSystem.getAPI().getRedisData().getDatabaseID());
            cloudPlayerAPI = gson.fromJson(jedis.hget(REDIS_CLOUD_PLAYERS_PATH + uuid, uuid.toString()), CloudPlayer.class);
        }
        return cloudPlayerAPI;
    }

    public static CloudPlayer getCloudPlayer(String playerName) {
        UUID uuid;
        try (Jedis jedis = CloudSystem.getAPI().getRedisPool().getResource()) {

            uuid = UUID.fromString(jedis.hget(REDIS_CLOUD_PLAYERS_NAME_PATH + playerName, "uuid"));

        }
        return getCloudPlayer(uuid);
    }

    @Override
    public void connect(String serverName) {
        CloudSystem.getEventAPI().callEvent(new ConnectCloudPlayerToServerEvent(uniqueID, serverName));
    }

    @Override
    public void connect(GameServerData gameServerData) {
        CloudSystem.getEventAPI().callEvent(new ConnectCloudPlayerToServerEvent(uniqueID, gameServerData.getServerName()));
    }

    @Override
    public String getGameServerName() {
        return getServerName();
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public CloudProxy getProxy() {
        return null;
    }

    @Override
    public void sendMessage(String message) {
        CloudSystem.getEventAPI().callEvent(new SendMessageToCloudPlayerEvent(uniqueID, message));
    }

    @Override
    public void kickPlayer(String message) {
        CloudSystem.getEventAPI().callEvent(new KickCloudPlayerEvent(uniqueID, message));
    }
}
