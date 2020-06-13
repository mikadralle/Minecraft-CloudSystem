package eu.unyfy.master.handler.wrapper.handler;

import eu.unyfy.master.MasterBootstrap;
import java.util.Timer;
import java.util.TimerTask;
import lombok.RequiredArgsConstructor;
import me.tomsdevsn.hetznercloud.objects.general.Server;
import me.tomsdevsn.hetznercloud.objects.response.ActionResponse;

@RequiredArgsConstructor
public class StartWrapperHandler implements Runnable {

  private final long serverID;


  @Override
  public void run() {

    MasterBootstrap.getInstance().getLogger().info("start thread...");

    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {

        Server server = MasterBootstrap.getInstance().getHetznerCloudAPI().getServerById(serverID).getServer();
        MasterBootstrap.getInstance().getLogger().info("Status: " + server.getStatus());
        if (server.getStatus().equalsIgnoreCase("running")) {
          ActionResponse action = MasterBootstrap.getInstance().getHetznerCloudAPI().softRebootServer(serverID);
          if (action != null) {
            action.getAction().setCommand("mkdir /home/test/mika/leantwi-" + System.currentTimeMillis() + "/");
            MasterBootstrap.getInstance().getLogger().info("Status: " + action.getAction().getStatus());
          } else {
            MasterBootstrap.getInstance().getLogger().info("null");
          }
        }


      }
    }, 10, 1000);


  }
}
