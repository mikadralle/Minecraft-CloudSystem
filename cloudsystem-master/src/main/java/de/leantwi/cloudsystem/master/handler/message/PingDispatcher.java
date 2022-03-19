package de.leantwi.cloudsystem.master.handler.message;

import de.leantwi.cloudsystem.master.MasterBootstrap;
import io.nats.client.Dispatcher;
import java.nio.charset.StandardCharsets;

public class PingDispatcher {


  public void listen() {

    Dispatcher pingDispatcher = MasterBootstrap.getInstance().getNatsConnector().getConnection().createDispatcher(message -> {
      String msg = new String(message.getData(), StandardCharsets.UTF_8);
      String[] split = msg.split("#");
      switch (split[0]) {

        case "ping":
          MasterBootstrap.getInstance().getNatsConnector().publish(message.getReplyTo(), "pong");
          System.out.println("PONG");
          break;
      }
    });

    pingDispatcher.subscribe("ping");

  }
}
