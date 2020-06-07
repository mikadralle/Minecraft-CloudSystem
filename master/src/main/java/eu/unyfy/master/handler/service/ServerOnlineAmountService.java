package eu.unyfy.master.handler.service;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.handler.core.Core;
import lombok.Getter;

@Getter
public class ServerOnlineAmountService implements Runnable {

  private final MasterBootstrap master = MasterBootstrap.getInstance();
  private final Core core = this.master.getCore();

  @Override
  public void run() {

    this.core.getGroupsDBList().forEach(groupDB -> groupDB.getSubGroupDBList().forEach(subGroupDB -> {

      int missServer = subGroupDB.getMissServerAmount();

      for (int i = 0; i < missServer; i++) {
        this.master.getServerFactory().createServer(subGroupDB.getSubGroupName());
      }

    }));

  }
}
