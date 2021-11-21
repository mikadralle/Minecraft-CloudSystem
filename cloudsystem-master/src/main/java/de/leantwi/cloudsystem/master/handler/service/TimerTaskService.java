package de.leantwi.cloudsystem.master.handler.service;

import de.leantwi.cloudsystem.master.handler.core.Core;
import de.leantwi.cloudsystem.master.handler.wrapper.WrapperHandler;
import de.leantwi.cloudsystem.master.MasterBootstrap;

import java.util.Timer;
import java.util.TimerTask;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TimerTaskService implements Runnable {

  private final MasterBootstrap master = MasterBootstrap.getInstance();
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