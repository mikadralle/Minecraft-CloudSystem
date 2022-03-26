package de.leantwi.cloudsystem.master.database.group;

import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.master.database.mongo.DBDocument;
import de.leantwi.cloudsystem.master.MasterBootstrap;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;

@Getter
@Setter
@RequiredArgsConstructor
public class SubGroupDB extends DBDocument {

  private final GroupDB groupDB;
  private String subGroupName;
  private ServerDB serverDB = new ServerDB();
  private List<GameServerData> startSessionServerList = new ArrayList<>();
  private List<GameServerData> sessionServerList = new ArrayList<>();


  @Override
  public Document create() {
    return new Document("subGroupName", this.subGroupName).append("server", this.serverDB.create());
  }

  @Override
  public void fetch(Document document) {
    if (document.containsKey("subGroupName")) {
      this.subGroupName = document.getString("subGroupName");
    }
    if (document.containsKey("server")) {
      this.serverDB.fetch(document.get("server", Document.class));
    }

    //Master.getInstance().getCore().getGroupDBStringMap().put(this.groupDB, this.subGroupName);
    MasterBootstrap.getInstance().getCore().getSubGroupDBString().put(this.subGroupName, this);
  }
/*
  public Integer getMissServerAmount() {
    return this.serverDB.getMinOnlineAmount() - (this.startSessionServerList.size() + this.sessionServerList.size());
  }

  public List<GameServerData> getAllServerList() {
    List<GameServerData> list = new ArrayList<>();
    list.addAll(this.startSessionServerList);
    list.addAll(this.sessionServerList);
    return list;
  }



 */
}
