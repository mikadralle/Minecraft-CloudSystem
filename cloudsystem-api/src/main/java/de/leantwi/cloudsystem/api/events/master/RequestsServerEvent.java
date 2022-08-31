package de.leantwi.cloudsystem.api.events.master;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestsServerEvent extends Event {

    private String subGroupName;
    private int amount;


}
