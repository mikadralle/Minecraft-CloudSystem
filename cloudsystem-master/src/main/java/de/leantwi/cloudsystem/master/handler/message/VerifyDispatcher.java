package de.leantwi.cloudsystem.master.handler.message;

import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.wrapper.WrapperType;
import io.nats.client.Dispatcher;

import java.nio.charset.StandardCharsets;

import lombok.Getter;

@Getter
public class VerifyDispatcher {

    private final MasterBootstrap master = MasterBootstrap.getInstance();


    public void listen() {

        Dispatcher verifyDispatcher = this.master.getNatsConnector().getConnection().createDispatcher(message -> {

            String msg = new String(message.getData(), StandardCharsets.UTF_8);
            String[] split = msg.split("#");
            switch (split[0]) {

                case "wrapper_register":

                    this.verifyWrapper(split, message.getReplyTo());

                    break;
                case "bungeecord_register":

                    final String bungeeName = this.master.getBungeeHandler().verifyBungeeCord();

                    this.master.getNatsConnector().publish(message.getReplyTo(), bungeeName);
                    this.master.getLogger().info("The bungeecord server " + bungeeName + " has benn registered.");
                    break;
            }
        });
        verifyDispatcher.subscribe("verify");


    }

    private void verifyWrapper(String[] msg, String replayTo) {


        String wrapperID = msg[1];
        WrapperType wrapperType = WrapperType.valueOf(msg[2]);
        int weightClass = Integer.parseInt(msg[3]);
        int priority = Integer.parseInt(msg[4]);

        String hostName = this.master.getWrapperHandler().verifyWrapper(wrapperID, wrapperType, weightClass, priority);
        this.master.getNatsConnector().publish(replayTo, hostName);

        switch (wrapperType) {
            case PRIVATE:
                this.master.getLogger().info("The private wrapper " + wrapperID + " has been verified.");
                break;
            case PUBLIC:
                this.master.getLogger().info("The public wrapper " + wrapperID + " has been verified.");
                break;
        }

    }

}
