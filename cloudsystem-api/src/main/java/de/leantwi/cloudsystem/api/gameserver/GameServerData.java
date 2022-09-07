package de.leantwi.cloudsystem.api.gameserver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter

@AllArgsConstructor
public class GameServerData {

    private String hostName, serverName, wrapperID, subGroupDB, groupDB;
    private int port, onlinePlayers, weightClass, maxOnlinePlayers;
    private boolean staticMode;
    private GameState gameState;
    private UUID serverID;




}
