package de.leantwi.cloudsystem.api.events.proxy;

import de.leantwi.cloudsystem.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StopProxyServerEvent extends Event {

    private String proxyID, shutdownMessage;
}
