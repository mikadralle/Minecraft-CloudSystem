package de.leantwi.cloudsystem.wrapper.core.handler;

import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;
import de.leantwi.cloudsystem.wrapper.core.GameServerHandler;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskHandler implements Runnable {

  private final GameServerHandler gameServerHandler = WrapperBootstrap.getInstance().getGameServerHandler();
  @Override
  public void run() {
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        if (WrapperBootstrap.getInstance().isMasterOnline()) {
         gameServerHandler.startServer();
        } else {
          WrapperBootstrap.getInstance().getLogger().info("§cMaster connection is broken...");
        }
      }
    }, 2000, 6000);

  }
}
