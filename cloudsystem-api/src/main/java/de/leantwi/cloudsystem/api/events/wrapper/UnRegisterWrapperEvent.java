package de.leantwi.cloudsystem.api.events.wrapper;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class UnRegisterWrapperEvent extends Event {

    private String wrapperID;

}
