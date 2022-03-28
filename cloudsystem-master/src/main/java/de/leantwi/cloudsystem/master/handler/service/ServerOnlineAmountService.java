package de.leantwi.cloudsystem.master.handler.service;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.gameserver.server.ServerDB;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.server.ServerFactory;
import lombok.Getter;

@Getter
public class ServerOnlineAmountService implements Runnable {

    private final MasterBootstrap master = MasterBootstrap.getInstance();
    private final ServerFactory serverFactory = getMaster().getServerFactory();
    private CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();

    @Override
    public void run() {

        this.cloudSystemAPI.getAllSubGroups().forEach(subGroupDB -> {
            ServerDB serverDB = subGroupDB.getServerDB();
            int missServerCount = (serverDB.getMinOnlineAmount() -
                    this.cloudSystemAPI.getAllGameServerBySubGroupName(subGroupDB.getSubGroupName()).size());

            for (int i = 0; i < missServerCount; i++) {
                this.getServerFactory().createServer(subGroupDB.getSubGroupName());
            }

        });

    }
}
