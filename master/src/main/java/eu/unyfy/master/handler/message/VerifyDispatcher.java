package eu.unyfy.master.handler.message;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.handler.packets.VerifyWrapperPacket;
import io.nats.client.Dispatcher;
import java.nio.charset.StandardCharsets;
import lombok.Getter;

@Getter
public class VerifyDispatcher {

  private final MasterBootstrap master = MasterBootstrap.getInstance();


  public void listen() {

    Dispatcher verifyDispatcher = this.master.getNatsConnector().getNatsConnection().createDispatcher(message -> {

      String msg = new String(message.getData(), StandardCharsets.UTF_8);
      String[] split = msg.split("#");
      switch (split[0]) {

        case "wrapper_register":
          this.master.getPacketHandler().callPacket(new VerifyWrapperPacket(msg + "#" + message.getReplyTo()));
          break;
      }
    });
    verifyDispatcher.subscribe("verify");


  }

}
