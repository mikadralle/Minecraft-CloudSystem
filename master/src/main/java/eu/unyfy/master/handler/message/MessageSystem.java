package eu.unyfy.master.handler.message;

import eu.unyfy.master.Master;
import eu.unyfy.master.database.nats.NatsConnector;
import eu.unyfy.master.handler.packets.CreateGroupPacket;
import eu.unyfy.master.handler.packets.PlayerConnectNetworkPacket;
import eu.unyfy.master.handler.packets.PlayerDisconnectServerPacket;
import eu.unyfy.master.handler.packets.PlayerSwitchServerPacket;
import eu.unyfy.master.handler.packets.RegisterBungeeCordPacket;
import eu.unyfy.master.handler.packets.RegisterWrapperPacket;
import eu.unyfy.master.handler.packets.ServerOfflinePacket;
import eu.unyfy.master.handler.packets.ServerOnlinePacket;
import eu.unyfy.master.handler.packets.StartGroupPacket;
import eu.unyfy.master.handler.packets.UnRegisterBungeeCordPacket;
import eu.unyfy.master.handler.packets.UnRegisterWrapperPacket;
import eu.unyfy.master.handler.packets.UpdateGameStatePacket;
import io.nats.client.Dispatcher;
import java.nio.charset.StandardCharsets;
import lombok.Getter;

@Getter
public class MessageSystem {

  private final Master master = Master.getInstance();
  private final NatsConnector natsConnector = this.master.getNatsConnector();

  public void listen() {

    final Dispatcher cloudDispatcher = natsConnector.getNatsConnection().createDispatcher(message -> {

      final String msg = new String(message.getData(), StandardCharsets.UTF_8);
      final String[] split = msg.split("#");
      System.out.println("receive: " + msg);
      switch (split[0]) {

        case "groupCreate":
          this.master.getPacketHandler().callPacket(new CreateGroupPacket(msg));
          break;
        case "updateGameState":
          this.master.getPacketHandler().callPacket(new UpdateGameStatePacket(msg));
          break;
        case "start_server":
          this.master.getPacketHandler().callPacket(new StartGroupPacket(msg));
          break;
        case "join_network_player":
          this.master.getPacketHandler().callPacket(new PlayerConnectNetworkPacket(msg));
          break;
        case "server_switch_player":
          this.master.getPacketHandler().callPacket(new PlayerSwitchServerPacket(msg));
          break;
        case "leave_server_player":
          this.master.getPacketHandler().callPacket(new PlayerDisconnectServerPacket(msg));
          break;
        case "wrapper_logout":
          this.master.getPacketHandler().callPacket(new UnRegisterWrapperPacket(msg));
          break;
        case "wrapper_login":
          this.master.getPacketHandler().callPacket(new RegisterWrapperPacket(msg));
          break;
        case "online":
          this.master.getPacketHandler().callPacket(new ServerOnlinePacket(msg));
          break;
        case "offline":
          this.master.getPacketHandler().callPacket(new ServerOfflinePacket(msg));
          break;
        case "login_bungeecord":
          this.master.getPacketHandler().callPacket(new RegisterBungeeCordPacket(msg));
          break;
        case "logout_bungeecord":
          this.master.getPacketHandler().callPacket(new UnRegisterBungeeCordPacket(msg));
          break;
        case "quit":
          this.master.getNatsConnector().sendMessage("cloud", "shutdownALL");
          break;

      }
    });
    cloudDispatcher.subscribe("cloud");

  }

}

