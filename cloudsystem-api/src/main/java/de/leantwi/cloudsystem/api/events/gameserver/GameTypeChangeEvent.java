package de.leantwi.cloudsystem.api.events.gameserver;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameTypeChangeEvent extends Event {

    private String gametype,serverName;


}
