package de.leantwi.cloudsystem.master.handler.message;

import de.leantwi.cloudsystem.master.handler.packets.StartGroupPacket;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.packets.CreateGroupPacket;
import io.nats.client.Dispatcher;

import java.nio.charset.StandardCharsets;

import lombok.Getter;

@Getter
public class CloudDispatcher {

    private final MasterBootstrap master = MasterBootstrap.getInstance();

    public void listen() {

        final Dispatcher cloudDispatcher = master.getNatsConnector().getConnection().createDispatcher(message -> {

            final String msg = new String(message.getData(), StandardCharsets.UTF_8);
            final String[] split = msg.split("#");
            switch (split[0]) {

                case "groupCreate":
                    this.master.getPacketHandler().callPacket(new CreateGroupPacket(msg));
                    break;
                case "start_server":
                    this.master.getPacketHandler().callPacket(new StartGroupPacket(msg));
                    break;
                case "login_bungeecord":
                    //  this.master.getPacketHandler().callPacket(new RegisterBungeeCordPacket(msg));
                    break;
                case "quit":
                    this.master.getNatsConnector().publish("cloud", "shutdownALL");
                    break;
            }
        });
        cloudDispatcher.subscribe("cloud");

    }


}

