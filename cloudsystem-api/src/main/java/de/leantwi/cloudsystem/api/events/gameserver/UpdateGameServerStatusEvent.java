package de.leantwi.cloudsystem.api.events.gameserver;

import de.leantwi.cloudsystem.api.event.Event;
import de.leantwi.cloudsystem.api.gameserver.GameState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateGameServerStatusEvent extends Event {

    private String gameStatus, serverName;

    public GameState getGameStatus(){
        return GameState.getGameStateByString(gameStatus);
    }



}
