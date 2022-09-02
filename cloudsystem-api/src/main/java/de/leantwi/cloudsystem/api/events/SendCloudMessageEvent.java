package de.leantwi.cloudsystem.api.events;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Deprecated
@AllArgsConstructor
@Getter
@Setter
public class SendCloudMessageEvent extends Event {

    private String message;

}
