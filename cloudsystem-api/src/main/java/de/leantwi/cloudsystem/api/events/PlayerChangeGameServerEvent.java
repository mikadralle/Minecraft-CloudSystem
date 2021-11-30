package de.leantwi.cloudsystem.api.events;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PlayerChangeGameServerEvent extends Event {


    private String message;
    private String targetServer;
    private String previousServer;


}
