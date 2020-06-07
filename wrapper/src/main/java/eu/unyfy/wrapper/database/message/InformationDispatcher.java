package eu.unyfy.wrapper.database.message;

import eu.unyfy.wrapper.WrapperBootstrap;
import eu.unyfy.wrapper.database.nats.NatsConnector;
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
