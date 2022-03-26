package de.leantwi.cloudsystem.master.handler.server;

import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.database.group.ServerDB;
import de.leantwi.cloudsystem.master.database.group.SubGroupDB;
import de.leantwi.cloudsystem.master.handler.core.Core;
import lombok.Getter;
import redis.clients.jedis.Jedis;


public class ServerFactory {

    private final MasterBootstrap master = MasterBootstrap.getInstance();


    private SubGroupDB subGroupDB;
    private ServerDB serverDB;
    @Getter
    private Core core = MasterBootstrap.getInstance().getCore();

    public void createServer(String subGroupName) {

        String serverName = subGroupName + "-" + getID(subGroupName);
        this.subGroupDB = master.getCore().getSubGroup(subGroupName);
        this.serverDB = subGroupDB.getServerDB();

        String wrapperName = this.master.getWrapperHandler().getRandomWrapper(subGroupName, serverDB.getWeightClass());
        System.out.println("ServerName: " + serverName);
        System.out.println("Wrapper-Name: " + wrapperName);
        if (wrapperName == null) {
            System.out.println("Wrapper is null!");
            return;
        }
        final GameState gameState = GameState.LOBBY;
        final int port = this.master.getWrapperHandler().getWrapperServer(wrapperName).getPort();
        final String hostName = this.master.getWrapperHandler().getWrapperServer(wrapperName).getHostName();
        //create class GameServerData
        GameServerData gameServerData = new GameServerData(hostName, serverName, wrapperName,
                subGroupDB.getSubGroupName(), subGroupDB.getGroupDB().getGroupName(),
                port, 0, 0, serverDB.getMaxPlayer(), gameState);
        //sets data into redis.
        gameServerData.updateSession(this.master.getRedisConnector().getJedisPool());
        this.master.getWrapperHandler().getWrapperServer(wrapperName).addServer(gameServerData);

        //send packet to wrapper, that create sessionServer
        //TODO: Replace under line in a new Call Event class.
        this.master.getNatsConnector().publish("cloud", "sessionServer#create#" + wrapperName + "#" + serverName);
        // put information in a core
        this.master.getCore().getCurrentSessionServer().put(serverName, gameServerData);
        this.getCore().getGameServerStartQueue().add(gameServerData);
        this.master.sendMessage("the server " + serverName + " will be started");
    }

    public void deleteServer(GameServerData gameServerData) {
        this.master.getWrapperHandler().getWrapperServer(gameServerData.getWrapperID()).removeServer(gameServerData);
        this.getCore().getGameServerList().remove(gameServerData);
        this.master.getCore().getCurrentSessionServer().remove(gameServerData.getServerName());
        gameServerData.deleteSession(this.master.getRedisConnector().getJedisPool());
    }

    public int getID(String subGroupName) {



        for (int i = 1; i < 999; i++) {
            if (!this.master.getCore().getCurrentSessionServer().containsKey(subGroupName.toLowerCase() + "-" + i)) {
                return i;
            }
        }
        return this.master.getCore().getCurrentSubGroupServerOnlineSize(subGroupName) + 1;
    }

    public void addOnlineList(String serverName) {
        try (Jedis jedis = this.master.getRedisConnector().getJedisPool().getResource()) {
            jedis.set("cloud:sessions:serverlist:" + serverName, serverName);
        }
    }

    public void removeOnlineList(String serverName) {
        try (Jedis jedis = this.master.getRedisConnector().getJedisPool().getResource()) {
            jedis.del("cloud:sessions:serverlist:" + serverName, serverName);
        }
    }

}
