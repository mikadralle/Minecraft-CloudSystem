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

  private final List<Integer> portList = new ArrayList<>();
  private final List<SessionServer> serverSessionList = new ArrayList<>();
  //
  private String hostName;
  private int weightClass;
  private boolean master;
  private String wrapperID;
  private int priority;
  private WrapperType wrapperType;

  public void fetch(String wrapperID, String hostName, WrapperType wrapperType, int weightClass, int priority) {
    this.wrapperID = wrapperID;
    this.hostName = hostName;
    this.wrapperType = wrapperType;
    this.weightClass = weightClass;
    this.priority = priority;

    for (int id = 26000; id < 27000; id++) {
      this.portList.add(id);
    }
  }

  public void addServer(SessionServer sessionServer) {
    this.serverSessionList.add(sessionServer);
    System.out.println("port: " + sessionServer.getPort());
    this.portList.remove((Integer) sessionServer.getPort());
    this.weightClass = this.weightClass - sessionServer.getWeightClass();
  }

  public void removeServer(SessionServer sessionServer) {
    this.serverSessionList.remove(sessionServer);
    this.portList.add(sessionServer.getPort());
    this.weightClass = weightClass + sessionServer.getWeightClass();
  }

  public Integer getPort() {
    return this.portList.get(new Random().nextInt(this.portList.size()));
  }


}

