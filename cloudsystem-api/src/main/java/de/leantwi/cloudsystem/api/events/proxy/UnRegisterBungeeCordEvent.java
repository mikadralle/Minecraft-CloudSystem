package de.leantwi.cloudsystem.api.events.proxy;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnRegisterBungeeCordEvent extends Event {

    private String bungeeName;

}
