package eu.unyfy.master.handler.message;

import eu.unyfy.master.MasterBootstrap;
import io.nats.client.Dispatcher;
import java.nio.charset.StandardCharsets;

public class PingDispatcher {


  public void listen() {

    Dispatcher pingDispatcher = MasterBootstrap.getInstance().getNatsConnector().getNatsConnection().createDispatcher(message -> {
      String msg = new String(message.getData(), StandardCharsets.UTF_8);
      String[] split = msg.split("#");
      switch (split[0]) {

        case "ping":
          MasterBootstrap.getInstance().getNatsConnector().sendMessage(message.getReplyTo(), "pong");
          break;
      }
    });

    pingDispatcher.subscribe("ping");

  }
}
