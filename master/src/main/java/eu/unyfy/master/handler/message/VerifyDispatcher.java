package eu.unyfy.master.handler.message;

import eu.unyfy.master.Master;
import eu.unyfy.master.handler.packets.VerifyWrapperPacket;
import io.nats.client.Dispatcher;
import java.nio.charset.StandardCharsets;
import lombok.Getter;

@Getter
public class VerifyDispatcher {

  private final Master master = Master.getInstance();


  public void listen() {

    Dispatcher verifyDispatcher = this.master.getNatsConnector().getNatsConnection().createDispatcher(message -> {

      String msg = new String(message.getData(), StandardCharsets.UTF_8);
      String[] split = msg.split("#");
      System.out.println("Channel: Verify -> Message: " + msg);
      switch (split[0]) {

        case "wrapper_register":
          System.out.println("Channel: Verify -> d: " + msg);
          this.master.getPacketHandler().callPacket(new VerifyWrapperPacket(msg + "#" + message.getReplyTo()));
          break;
      }
    });
    verifyDispatcher.subscribe("verify");


  }

}
