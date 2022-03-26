package de.leantwi.cloudsystem.master.handler.core;

import com.google.gson.Gson;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.master.database.group.ServerDB;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.database.group.GroupDB;
import de.leantwi.cloudsystem.master.database.group.SubGroupDB;
import de.leantwi.cloudsystem.master.handler.service.ServerOnlineAmountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import lombok.Getter;
import redis.clients.jedis.Jedis;

public class Core {

    //

    @Getter

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    @Getter
    private final MasterBootstrap master = MasterBootstrap.getInstance();

    private final Gson gson = new Gson();

    private final List<GroupDB> groupsDBList = new ArrayList<>(); //GroupDB,SubGroup

    //  private final ConcurrentHashMap<GroupDB, String> groupDBStringMap = new ConcurrentHashMap<>();//GroupDB,SubGroupString
    private final ConcurrentHashMap<String, SubGroupDB> subGroupDBString = new ConcurrentHashMap<>();//SubGroupString,SubGroupDB

    // Core

    public void startService() {
        this.executorService.execute(new ServerOnlineAmountService());
    }

    public List<GameServerData> getGameServerListByServerName(String serverName){

        try (Jedis jedis = this.getMaster().getRedisConnector().getJedisPool().getResource()){
        //cloud:sessions:serverlist


        }

        return null;
    }

    @Deprecated
    public GameServerData getSessionServer(String serverName) {
        return this.gameServerList.stream().filter(f -> f.getServerName().equalsIgnoreCase(serverName)).findFirst().get();
    }
    public Optional<GameServerData> getGameServerByServerName(String serverName) {
        return this.gameServerList.stream().filter(f -> f.getServerName().equalsIgnoreCase(serverName)).findFirst();
    }

    public GroupDB getGroupDBByName(String groupDBName) {
        return this.master.getGroupHandler().getGroups().get(groupDBName);
    }

    public Integer getCurrentSubGroupServerOnlineSize(String subGroupName) {

        if (getAllGameServerBySubGroup(subGroupName).isEmpty()) {
            return 0;
        }

        return this.getAllGameServerBySubGroup(subGroupName).size();
    }

    public List<GameServerData> getAllGameServerBySubGroup(String subGroupName) {
        return this.gameServerList.stream().filter(f -> f.getSubGroupDB().equalsIgnoreCase(subGroupName)).collect(Collectors.toList());
    }

    public GroupDB getGroupDBFromSubGroup(String subGroupName) {
        return this.groupsDBList.stream().filter(groupDB -> groupDB.getSubGroupDBList().contains(this.subGroupDBString.get(subGroupName))).findFirst().get();
    }

    public ServerDB getServerDBBySubGroupName(String subGroupName) {
        return getSubGroup(subGroupName).getServerDB();
    }

    public SubGroupDB getSubGroup(String subGroupName) {

        return getGroupDBFromSubGroup(subGroupName).getSubGroupDBList().stream().filter(subGroupDB ->
                subGroupDB.getSubGroupName().equalsIgnoreCase(subGroupName)).findFirst().orElse(null);
    }

    public boolean exitsSubGroupByName(String subGroupName) {
        return getGroupDBFromSubGroup(subGroupName).getSubGroupDBList().stream().anyMatch(subGroupDB ->
                subGroupDB.getSubGroupName().equalsIgnoreCase(subGroupName));
    }

    public Integer getMissServerAmount(String subGroupName) {

        SubGroupDB subGroup = this.getSubGroup(subGroupName);
        ServerDB server = subGroup.getServerDB();

        return Math.toIntExact(server.getMinOnlineAmount() - this.getAllGameServerBySubGroup(subGroupName).size());
    }
}
