package de.leantwi.cloudsystem.api.events.groups;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshGroupEvent extends Event {

    private final String targetGroupName, targetSubGroupName;
}
