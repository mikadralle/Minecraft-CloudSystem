package de.leantwi.cloudsystem.api.events.player;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class KickCloudPlayerEvent extends Event {
    private UUID uuid;
    private String kickMessage;


}
