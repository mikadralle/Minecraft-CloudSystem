package eu.unyfy.master.handler.packets;

import eu.unyfy.master.Master;
import eu.unyfy.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class UnRegisterBungeeCordPacket extends Packet {

  private final Master master = Master.getInstance();
  //   private Core core = master.getCore();

  public UnRegisterBungeeCordPacket(String message) {
    super(message);
  }

  @Override
  public void execute() {
    String bungeeName = getStrings()[1];
    this.master.getBungeeHandler().getBungeeList().remove(bungeeName);
    Master.getInstance().getConsole().sendMessage("the bungeecord server '" + bungeeName + "' has been unregistered.");
  }
}
