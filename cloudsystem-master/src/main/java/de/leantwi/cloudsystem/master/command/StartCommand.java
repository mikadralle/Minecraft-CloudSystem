package de.leantwi.cloudsystem.master.command;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.events.PlayerChangeGameServerEvent;
import de.leantwi.cloudsystem.api.events.SendCloudMessageEvent;
import de.leantwi.cloudsystem.master.handler.hoster.HetnerType;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.service.command.CommandImplementation;

public class StartCommand implements CommandImplementation {

  @Override
  public void execute(String[] strings) {

    //start <cx11>

    CloudSystem.getEventAPI().callEvent(new PlayerChangeGameServerEvent("Nachricht","lobby-01","buildffa-01"));

    if (strings.length == 1) {

      String type = strings[0];
      //MasterBootstrap.getInstance().getWrapperHandler().createServer(HetnerType.CX11);
    MasterBootstrap.getInstance().sendMessage("Disabled due system error");
      CloudSystem.getEventAPI().callEvent(new SendCloudMessageEvent("Hetzter cloud could not start!"));
    }

  }

  @Override
  public String getName() {
    return "start";
  }

  @Override
  public boolean isUsageRight(String[] strings) {
    return true;
  }

  @Override
  public String getUsage() {
    return "start - starts a hetzter cloud server.";
  }
}
