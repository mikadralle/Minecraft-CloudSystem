package de.leantwi.cloudsystem.master.handler.server;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.ICloudSystem;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.database.group.ServerDB;
import de.leantwi.cloudsystem.master.database.group.SubGroupDB;
import de.leantwi.cloudsystem.master.handler.core.Core;
import lombok.Getter;
import redis.clients.jedis.Jedis;

import java.util.Optional;


public class ServerFactory {

    private final MasterBootstrap master = MasterBootstrap.getInstance();


    private SubGroupDB subGroupDB;
    private ServerDB serverDB;
    private final ICloudSystem cloudSystem = CloudSystem.getAPI();
    @Getter
    private Core core = MasterBootstrap.getInstance().getCore();

    public void createServer(String subGroupName) {

        String serverName = subGroupName + "-" + getID(subGroupName);
        this.subGroupDB = master.getCore().getSubGroup(subGroupName);
        this.serverDB = subGroupDB.getServerDB();

        String wrapperName = this.master.getWrapperHandler().getRandomWrapper(subGroupName, serverDB.getWeightClass());
        if (wrapperName == null) {
            System.out.println("Wrapper is null!");
            return;
        }
        final int port = this.master.getWrapperHandler().getWrapperServer(wrapperName).getPort();
        final String hostName = this.master.getWrapperHandler().getWrapperServer(wrapperName).getHostName();
        //create class GameServerData
        GameServerData gameServerData = new GameServerData(hostName, serverName, wrapperName,
                subGroupDB.getSubGroupName(), subGroupDB.getGroupDB().getGroupName(),
                port, 0, 0, serverDB.getMaxPlayer(), GameState.STARTS);
        //sets data into redis.
        this.cloudSystem.updateGameServer(gameServerData);
        this.master.getNatsConnector().publish("cloud", "sessionServer#create#" + wrapperName + "#" + serverName);
        this.master.getWrapperHandler().getWrapperServer(wrapperName).addServer(gameServerData);

        this.master.sendMessage("the server " + serverName + " will be started");
    }

    public void deleteServer(GameServerData gameServerData) {
        this.master.getWrapperHandler().getWrapperServer(gameServerData.getWrapperID()).removeServer(gameServerData);
        cloudSystem.deleteGameServer(gameServerData);
    }

    public int getID(String subGroupName) {


        for (int i = 1; i < 999; i++) {
            final int b = i;
            Optional<GameServerData> gameServerData = cloudSystem.getAllGameServerBySubGroupName(subGroupName).stream().filter(f -> f.getSubGroupDB().toLowerCase().equalsIgnoreCase(subGroupName+"-"+ b)).findAny();
            if(!gameServerData.isPresent()){
                return i;
            }

        }
        return this.cloudSystem.getAllGameServerBySubGroupName(subGroupName).size() + 1;
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
