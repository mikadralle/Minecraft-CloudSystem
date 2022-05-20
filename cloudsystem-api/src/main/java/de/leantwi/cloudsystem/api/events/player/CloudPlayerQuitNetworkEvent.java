package de.leantwi.cloudsystem.api.events.player;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class CloudPlayerQuitNetworkEvent extends Event {

    private final UUID uniqueID;


}
