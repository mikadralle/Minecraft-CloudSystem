package de.leantwi.cloudsystem.api.events.gameserver;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class StopGameServerEvent extends Event {

    private String serverName;
}
