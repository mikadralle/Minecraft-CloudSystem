package de.leantwi.cloudsystem.master.handler.service;

import de.leantwi.cloudsystem.master.handler.core.Core;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import lombok.Getter;

@Getter
public class ServerOnlineAmountService implements Runnable {

    private final MasterBootstrap master = MasterBootstrap.getInstance();
    private final Core core = this.master.getCore();

    @Override
    public void run() {

        this.core.getGroupsDBList().forEach(groupDB -> groupDB.getSubGroupDBList().forEach(subGroupDB -> {
            int missServer = this.getCore().getMissServerAmount(subGroupDB.getSubGroupName());
            for (int i = 0; i < missServer; i++) {
                this.master.getServerFactory().createServer(subGroupDB.getSubGroupName());
            }

        }));

    }
}
