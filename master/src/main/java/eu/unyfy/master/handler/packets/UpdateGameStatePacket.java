package eu.unyfy.master.handler.packets;

import eu.unyfy.master.MasterBootstrap;
import eu.unyfy.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class UpdateGameStatePacket extends Packet {

  private final MasterBootstrap master = MasterBootstrap.getInstance();

  /* private Core core = master.getCore();
   private ServerCore serverCore;
   private ServerData serverData;
   private String serverName;
   private String state;
*/
  public UpdateGameStatePacket(String message) {
    super(message);
  }

  @Override
  public void execute() {
/*
        this.serverName = getStrings()[1];
        this.state = getStrings()[2];

        if (this.state.equalsIgnoreCase("restart")) {
            setState(GameType.RESTART);
            return;
        }

        if (this.state.equalsIgnoreCase("LOBBY")) {
            setState(GameType.LOBBY);
            return;
        }

        if (this.state.equalsIgnoreCase("NORMAL")) {
            setState(GameType.NORMAL);
            return;

        }

        if (this.state.equalsIgnoreCase("inGame")) {


            this.serverCore = this.core.getServerCore(this.serverName);
            this.serverCore.setGameType(GameType.INGAME);

            this.serverData = this.serverCore.getServerData();
            this.serverData.updateServerCore(serverCore);
            this.serverData.removeOnlineSize();
            this.serverData.getGroupData().updateServerData(this.serverData);
            return;
        }

*/
  }
/*
    private void setState(GameType gameType) {

        this.serverCore = this.core.getServerCore(this.serverName);
        this.serverCore.setGameType(gameType);
        this.serverCore.getServerData().updateServerCore(this.serverCore);

    }


 */
}
