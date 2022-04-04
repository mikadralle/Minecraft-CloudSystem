package de.leantwi.cloudsystem.wrapper.database.message;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;
import io.nats.client.Dispatcher;
import java.nio.charset.StandardCharsets;

public class InformationDispatcher {


  private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();

  public void listen() {

    Dispatcher infoDispatcher = this.cloudSystemAPI.getNatsConnector().getConnection().createDispatcher(message -> {

      String msg = new String(message.getData(), StandardCharsets.UTF_8);
      String[] split = msg.split("#");

      switch (split[0]) {
        case "master_connected":
          WrapperBootstrap.getInstance().loadWrapper();
          WrapperBootstrap.getInstance().getLogger().info("§cload Wrapper data. This function was triggered in class InformationDispatcher");

          break;
      }

    });
    infoDispatcher.subscribe("info");

  }

}
