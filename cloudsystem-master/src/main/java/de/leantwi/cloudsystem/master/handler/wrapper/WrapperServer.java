package de.leantwi.cloudsystem.master.handler.wrapper;

import de.leantwi.cloudsystem.api.gameserver.GameServerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WrapperServer {

    private final List<Integer> portList = new ArrayList<>();
  //  private final List<GameServerData> serverSessionList = new ArrayList<>();
    //
    private String hostName;
    private int weightClass;
    private boolean master;
    private String wrapperID;
    private int priority;
    private WrapperType wrapperType;

    public void fetch(String wrapperID, String hostName, WrapperType wrapperType, int weightClass, int priority) {
        this.wrapperID = wrapperID;
        this.hostName = hostName;
        this.wrapperType = wrapperType;
        this.weightClass = weightClass;
        this.priority = priority;

        for (int id = 26000; id < 27000; id++) {
            this.portList.add(id);
        }
    }

    public void addServer(GameServerData gameServerData) {
        this.portList.remove((Integer) gameServerData.getPort());
        this.weightClass = this.weightClass - gameServerData.getWeightClass();
    }

    public void removeServer(GameServerData sessionServer) {

        this.portList.add(sessionServer.getPort());
        this.weightClass = weightClass + sessionServer.getWeightClass();

    }

    public boolean isFreeSlot(String subGroupName) {
    //    long count = this.serverSessionList.stream().filter(sessionServer -> sessionServer.getSubGroupDB().equalsIgnoreCase(subGroupName)).count();
        long count = 1;
        return count <= 1;
    }

    public Integer getPort() {
        return this.portList.get(new Random().nextInt(this.portList.size()));
    }


}

