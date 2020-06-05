package eu.unyfy.master.handler.wrapper;

import eu.unyfy.master.handler.server.SessionServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WrapperServer {

  private final Random random = new Random();
  private final List<Integer> portList = new ArrayList<>();
  private final List<SessionServer> serverSessionList = new ArrayList<>();
  private String hostName;
  private Integer memorySize = 0;

  public void fetch(int memory, String hostName) {
    this.memorySize = memory;
    this.hostName = hostName;
    for (int id = 26000; id < 27000; id++) {
      this.portList.add(id);
    }
  }

  public void addServer(SessionServer sessionServer) {
    this.serverSessionList.add(sessionServer);
    System.out.println("port: " + sessionServer.getPort());
    this.portList.remove((Integer) sessionServer.getPort());
    this.memorySize = this.memorySize - sessionServer.getMemory();
  }

  public void removeServer(SessionServer sessionServer) {
    this.serverSessionList.remove(sessionServer);
    this.portList.add(sessionServer.getPort());
    this.memorySize = memorySize + sessionServer.getMemory();
  }

  public Integer getPort() {
    return this.portList.get(random.nextInt(this.portList.size()));
  }
}
