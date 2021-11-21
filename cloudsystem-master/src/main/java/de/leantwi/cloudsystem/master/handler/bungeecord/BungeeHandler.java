package de.leantwi.cloudsystem.master.handler.bungeecord;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BungeeHandler {

  private final List<String> bungeeList = new ArrayList<>();


  public String verifyBungeeCord() {
    String bungeeID = "bungeecord-" + getID();
    this.bungeeList.add(bungeeID);
    return bungeeID;
  }

  public void removeBungeeCord(String bungeeName) {
    this.bungeeList.remove(bungeeName);
  }


  public int getID() {

    for (int i = 1; i < 999; i++) {
      if (!this.bungeeList.contains("bungeecord-" + i)) {
        return i;
      }
    }
    return this.bungeeList.size() + 1;
  }


}
