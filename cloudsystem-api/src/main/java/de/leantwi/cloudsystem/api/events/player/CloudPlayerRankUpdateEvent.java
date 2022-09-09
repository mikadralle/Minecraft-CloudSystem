package de.leantwi.cloudsystem.api.events.player;

import de.leantwi.cloudsystem.api.event.Event;

import java.util.UUID;

@Deprecated
public class CloudPlayerRankUpdateEvent extends Event {

    private UUID uniqueID;
    private String rank;


}
