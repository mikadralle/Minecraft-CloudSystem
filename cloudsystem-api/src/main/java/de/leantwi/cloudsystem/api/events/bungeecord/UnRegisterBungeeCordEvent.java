package de.leantwi.cloudsystem.api.events.bungeecord;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnRegisterBungeeCordEvent extends Event {

    private String bungeeName;

}
