package eu.unyfy.master.handler.service;

import eu.unyfy.master.Master;
import eu.unyfy.master.handler.core.Core;
import lombok.Getter;

@Getter
public class ServerOnlineAmountService implements Runnable {

  private final Master master = Master.getInstance();
  private final Core core = this.master.getCore();

  @Override
  public void run() {

    this.core.getGroupsDBList().forEach(groupDB -> groupDB.getSubGroupDBList().forEach(subGroupDB -> {

      int onlineSize = subGroupDB.getSessionServerList().size();
      int missServer = subGroupDB.getMissServerAmount();

      for (int i = 0; i < missServer; i++) {
        //  System.out.println("start server  " + subGroupDB.getSubGroupName() + i);
        Master.getInstance().getServerFactory().createServer(subGroupDB.getSubGroupName());
      }

    }));

  }
}
