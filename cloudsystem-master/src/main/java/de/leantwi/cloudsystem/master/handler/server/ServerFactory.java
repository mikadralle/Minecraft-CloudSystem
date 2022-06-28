package de.leantwi.cloudsystem.master.handler.server;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.events.gameserver.RequestGameServerEvent;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import de.leantwi.cloudsystem.api.gameserver.groups.SubGroupDB;
import de.leantwi.cloudsystem.api.gameserver.server.ServerDB;
import de.leantwi.cloudsystem.master.MasterBootstrap;


public class ServerFactory {

    private final MasterBootstrap master = MasterBootstrap.getInstance();


    private final CloudSystemAPI cloudSystem = CloudSystem.getAPI();


    public void createServer(String subGroupName) {
        MasterBootstrap.getInstance().getLogger().info("Create: " + subGroupName);
        final SubGroupDB subGroupDB = this.cloudSystem.getSubGroupByName(subGroupName).get();
        final ServerDB serverDB = subGroupDB.getServerDB();

        final String wrapperName = this.master.getWrapperHandler().getRandomWrapper(subGroupName, serverDB.getMemory());
        System.out.println("Wrapper-Name: " + wrapperName);
        final String serverName = subGroupName + "-" + getID(subGroupName);

        if (wrapperName == null) {
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

        CloudSystem.getEventAPI().callEvent(new RequestGameServerEvent(wrapperName,serverName));

        this.master.getWrapperHandler().getWrapperServer(wrapperName).addServer(gameServerData);

        this.master.sendMessage("the server " + serverName + " will be started");
    }


    public int getID(String subGroupName) {

        if (cloudSystem.getAllGameServerBySubGroupName(subGroupName).isEmpty()) {
            return 1;
        }

        for (int i = 1; i < 9999; i++) {

            for (GameServerData gameServerData : cloudSystem.getAllGameServerBySubGroupName(subGroupName)) {
                if (!gameServerData.getServerName().equalsIgnoreCase(subGroupName + "-" + i)) {
                    return i;
                }
            }
        }
        return this.cloudSystem.getAllGameServerBySubGroupName(subGroupName).size() + 1;
    }


}
