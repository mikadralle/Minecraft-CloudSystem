package eu.unyfy.master.handler.packets;

import eu.unyfy.master.Master;
import eu.unyfy.master.handler.packets.handler.Packet;
import lombok.Getter;

@Getter
public class UnRegisterWrapperPacket extends Packet {

    private final Master master = Master.getInstance();

    public UnRegisterWrapperPacket(String message) {
        super(message);
    }

    @Override
    public void execute() {

        //wrapper
        String wrapperID = getStrings()[1];

        Master.getInstance().getWrapperHandler().logoutWrapper(wrapperID);

    }
}
