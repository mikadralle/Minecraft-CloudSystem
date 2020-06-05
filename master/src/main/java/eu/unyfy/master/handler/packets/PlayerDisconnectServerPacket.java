package eu.unyfy.master.handler.packets;

import eu.unyfy.master.Master;
import eu.unyfy.master.handler.packets.handler.Packet;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PlayerDisconnectServerPacket extends Packet {

    private final Master master = Master.getInstance();
    //  private ServerCore serverCore;

    public PlayerDisconnectServerPacket(String message) {
        super(message);
    }

    @Override
    public void execute() {

        UUID uuid = UUID.fromString(getStrings()[1]);
        String serverName = getStrings()[2];

       /* this.serverCore = Master.getInstance().getCore().getServerCore(serverName);
        this.serverCore.removePlayer(uuid);
        this.serverCore.getServerData().updateServerCore(this.serverCore);
*/
        Master.getInstance().getConsole().sendMessage("The Player " + uuid + " disconnect from " + serverName);

    }

}
