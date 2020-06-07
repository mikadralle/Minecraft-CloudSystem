package eu.unyfy.wrapper.core.handler;

import eu.unyfy.wrapper.WrapperBootstrap;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskHandler implements Runnable {

  @Override
  public void run() {
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        if (WrapperBootstrap.getInstance().isMasterOnline()) {
          WrapperBootstrap.getInstance().getWrapperCore().startServer();
        } else {
          WrapperBootstrap.getInstance().getLogger().info("§cMaster connection is broken...");
        }
      }
    }, 2000, 6000);

  }
}
