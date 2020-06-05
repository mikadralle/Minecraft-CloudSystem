package eu.unyfy.master.handler.packets.handler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Packet {

  private String message;
  private String[] strings;

  public Packet(String message) {
    this.message = message;
    this.strings = this.message.split("#");
  }

  public abstract void execute();

}
