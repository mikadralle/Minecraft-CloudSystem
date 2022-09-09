package de.leantwi.cloudsystem.api.events.player;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Deprecated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CloudPlayerRankUpdateEvent extends Event {

    private UUID uniqueID;
    private String rank;


}
