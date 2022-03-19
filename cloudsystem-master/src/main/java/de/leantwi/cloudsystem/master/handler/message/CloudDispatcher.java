package de.leantwi.cloudsystem.master.handler.message;

import de.leantwi.cloudsystem.master.handler.packets.ServerOnlinePacket;
import de.leantwi.cloudsystem.master.handler.packets.StartGroupPacket;
import de.leantwi.cloudsystem.master.handler.packets.UnRegisterBungeeCordPacket;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.handler.packets.CreateGroupPacket;
import de.leantwi.cloudsystem.master.handler.packets.PlayerConnectNetworkPacket;
import de.leantwi.cloudsystem.master.handler.packets.PlayerDisconnectServerPacket;
import de.leantwi.cloudsystem.master.handler.packets.PlayerSwitchServerPacket;
import de.leantwi.cloudsystem.master.handler.packets.ServerOfflinePacket;
import de.leantwi.cloudsystem.master.handler.packets.UnRegisterWrapperPacket;
import de.leantwi.cloudsystem.master.handler.packets.UpdateGameStatePacket;
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
        case "online":
          this.master.getPacketHandler().callPacket(new ServerOnlinePacket(msg));
          break;
        case "offline":
          this.master.getPacketHandler().callPacket(new ServerOfflinePacket(msg));
          break;
        case "login_bungeecord":
          //  this.master.getPacketHandler().callPacket(new RegisterBungeeCordPacket(msg));
          break;
        case "logout_bungeecord":
          this.master.getPacketHandler().callPacket(new UnRegisterBungeeCordPacket(msg));
          break;
        case "quit":
          this.master.getNatsConnector().publish("cloud", "shutdownALL");
          break;
      }
    });
    cloudDispatcher.subscribe("cloud");

  }


}

