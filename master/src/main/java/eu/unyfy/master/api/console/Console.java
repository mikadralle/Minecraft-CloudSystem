package eu.unyfy.master.api.console;

import eu.unyfy.master.Master;


public class Console {


  private final String PREFIX = "Cloud : ";
  private final Master master = Master.getInstance();


  public void sendMessage(String message) {
    System.out.println(this.PREFIX + message);
  }


  public void sendMessage(String message, boolean prefix) {
    if (prefix) {
      message = this.PREFIX + message;
    }
    System.out.println(message);
  }


}
