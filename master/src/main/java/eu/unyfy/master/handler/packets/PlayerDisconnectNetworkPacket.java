package eu.unyfy.master.handler.packets;

import eu.unyfy.master.Master;
import eu.unyfy.master.handler.packets.handler.Packet;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PlayerDisconnectNetworkPacket extends Packet {

    private final Master master = Master.getInstance();
    //  private Player player;

    public PlayerDisconnectNetworkPacket(String message) {
        super(message);
    }

    @Override
    public void execute() {

        UUID uuid = UUID.fromString(getStrings()[1]);
    /*    player = getMaster().getPlayer();

        ServerCore serverCore = player.getServerCore(uuid);
        serverCore.removePlayer(uuid);
        player.clearPlayer(uuid);
*/
        Master.getInstance().getConsole().sendMessage("The Player " + uuid + " disconnected from the network.");
    }
}
