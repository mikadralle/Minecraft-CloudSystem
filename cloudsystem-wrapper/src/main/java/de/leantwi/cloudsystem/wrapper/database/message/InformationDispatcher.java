package de.leantwi.cloudsystem.wrapper.database.message;

import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;
import de.leantwi.cloudsystem.wrapper.database.nats.NatsConnector;
import io.nats.client.Dispatcher;
import java.nio.charset.StandardCharsets;

public class InformationDispatcher {


  private final NatsConnector natsConnector = WrapperBootstrap.getInstance().getNatsConnector();

  public void listen() {

    Dispatcher infoDispatcher = natsConnector.getNatsConnection().createDispatcher(message -> {

      String msg = new String(message.getData(), StandardCharsets.UTF_8);
      String[] split = msg.split("#");

      switch (split[0]) {
        case "master_connected":
          WrapperBootstrap.getInstance().loadWrapper();
          break;
      }

    });
    infoDispatcher.subscribe("info");

  }

}
