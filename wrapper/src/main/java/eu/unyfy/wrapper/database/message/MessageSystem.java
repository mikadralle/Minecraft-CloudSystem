package eu.unyfy.wrapper.database.message;

import eu.unyfy.wrapper.Wrapper;
import eu.unyfy.wrapper.database.nats.NatsConnector;
import io.nats.client.Dispatcher;
import java.nio.charset.StandardCharsets;

public class MessageSystem {

  private final Wrapper wrapper = Wrapper.getInstance();
  private final NatsConnector natsConnector = this.wrapper.getNatsConnector();

  public void listen() {

    Dispatcher cloudDispatcher = natsConnector.getNatsConnection().createDispatcher(message -> {

      final String msg = new String(message.getData(), StandardCharsets.UTF_8);
      final String[] split = msg.split("#");
      System.out.println("Channel: cloud -> " + msg);
      switch (split[0]) {
        case "sessionServer":
          System.out.println("Wrapper-ID: " + this.wrapper.getWrapperSettings().getWrapperID());
          if (!split[1].equalsIgnoreCase("create")) {
            return;
          }
          if (!this.wrapper.getWrapperSettings().getWrapperID().equalsIgnoreCase(split[2])) {
            return;
          }
          System.out.println("Hello im here and you?");
          String serverName = split[3];
          this.wrapper.getWrapperCore().addWrapperList(serverName);
          break;

        case "quit":
          this.wrapper.onDisable();
          System.exit(0);
          break;
      }

    });

    cloudDispatcher.subscribe("cloud");

  }
}
