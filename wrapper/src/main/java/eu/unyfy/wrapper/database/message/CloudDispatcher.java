package eu.unyfy.wrapper.database.message;

import eu.unyfy.wrapper.WrapperBootstrap;
import eu.unyfy.wrapper.database.nats.NatsConnector;
import io.nats.client.Dispatcher;
import java.nio.charset.StandardCharsets;

public class CloudDispatcher {

  private final WrapperBootstrap wrapper = WrapperBootstrap.getInstance();
  private final NatsConnector natsConnector = this.wrapper.getNatsConnector();

  public void listen() {

    Dispatcher cloudDispatcher = natsConnector.getNatsConnection().createDispatcher(message -> {

      final String msg = new String(message.getData(), StandardCharsets.UTF_8);
      final String[] split = msg.split("#");
      switch (split[0]) {
        case "sessionServer":
          if (!split[1].equalsIgnoreCase("create")) {
            return;
          }
          if (!this.wrapper.getWrapperSettings().getWrapperID().equalsIgnoreCase(split[2])) {
            return;
          }
          String serverName = split[3];
          this.wrapper.getWrapperCore().addWrapperList(serverName);
          break;

        case "stop":
          //stop all
          if (split[1].equalsIgnoreCase("all") || split[1].equalsIgnoreCase("wrapper")) {
            this.wrapper.onShutdown();
            break;
          }
          WrapperBootstrap.getInstance().setMasterOnline(false);
          break;

      }

    });

    cloudDispatcher.subscribe("cloud");

  }
}
