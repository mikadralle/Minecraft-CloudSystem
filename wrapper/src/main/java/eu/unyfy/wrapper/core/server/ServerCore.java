package eu.unyfy.wrapper.core.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ServerCore {

  private final String serverName;
  private final String groupName;
  private final String mainGroup;
  private final boolean reset;
  private final String host;
  private final int port;
  private final int slots;
  private final int memory;


}
