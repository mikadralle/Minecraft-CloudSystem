package eu.unyfy.master.handler.packets;

import eu.unyfy.master.Master;
import eu.unyfy.master.handler.packets.handler.Packet;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PlayerSwitchServerPacket extends Packet {

    private final Master master = Master.getInstance();
    //   private ServerCore serverCore;

    public PlayerSwitchServerPacket(String message) {
        super(message);
    }

    @Override
    public void execute() {

        UUID uuid = UUID.fromString(getStrings()[1]);
        String serverName = getStrings()[2];

    /*    this.serverCore = getMaster().getCore().getServerCore(serverName);
        this.serverCore.addPlayer(uuid);

        Player player = getMaster().getPlayer();
        player.setServerInfo(uuid, serverName);
      */
        Master.getInstance().getConsole().sendMessage("The Player " + uuid + " connect to " + serverName);

    }
}
