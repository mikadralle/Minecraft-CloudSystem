package eu.unyfy.master.handler.service;

import eu.unyfy.master.Master;
import eu.unyfy.master.handler.core.Core;
import eu.unyfy.master.handler.wrapper.WrapperHandler;
import java.util.Timer;
import java.util.TimerTask;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TimerTaskService implements Runnable {

  private final Master master = Master.getInstance();
  private final WrapperHandler wrapperHandler = this.master.getWrapperHandler();
  private final Core core;

  @Override
  public void run() {

    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {

        if (wrapperHandler.isWrapperOnline()) {
          core.startService();
        }

      }
    }, 2000, 2000);
  }

}
