package de.leantwi.cloudsystem.api.events.global;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ShutdownSystemEvent extends Event {

    private String shutdownMessage;

}

