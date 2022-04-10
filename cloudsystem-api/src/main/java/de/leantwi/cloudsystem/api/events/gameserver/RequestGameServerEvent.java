package de.leantwi.cloudsystem.api.events.gameserver;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RequestGameServerEvent extends Event {

    private String wrapperID,serverName;



}
