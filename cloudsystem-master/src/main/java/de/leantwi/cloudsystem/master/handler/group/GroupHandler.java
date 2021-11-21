package de.leantwi.cloudsystem.master.handler.group;

import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.cloudsystem.master.database.group.GroupDB;
import de.leantwi.cloudsystem.master.database.group.ServerDB;
import de.leantwi.cloudsystem.master.database.group.SubGroupDB;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

@Getter
@Setter
public class GroupHandler {

  private Map<String, GroupDB> groups = new HashMap<>();
  private MasterBootstrap master = MasterBootstrap.getInstance();

  public void fetch() {

    this.master.getMongoDBConnector().getMongoDatabase().getCollection("groups").find().iterator().forEachRemaining(document -> {
      GroupDB groupDB = new GroupDB();
      groupDB.fetch(document);
      this.groups.put(groupDB.getGroupName(), groupDB);
    });

    if (!this.groups.isEmpty()) {
      this.master.sendMessage("Groups is loaded susses.");
      return;
    }

    this.master.sendMessage("couldn't find group! create a new group");
    createDefault("lobby", "lobby");

  }

  public void createGroup(String groupName, String subGroupName) {

    if (!this.groups.containsKey(groupName)) {
      createDefault(groupName, subGroupName);
      return;
    }

    this.groups.get(groupName).getSubGroupDBList().add(createSubGroup(subGroupName));
    this.master.getMongoDBConnector().getMongoDatabase().getCollection("groups").updateOne(new Document("groupName", groupName), new Document("$set", groups.get(groupName).create()));
    this.groups.put(groups.get(groupName).getGroupName(), groups.get(groupName));

  }

  private void createDefault(String groupName, String subGroupName) {

    GroupDB groupDB = new GroupDB();
    groupDB.setGroupName(groupName);

    groupDB.getSubGroupDBList().add(createSubGroup(subGroupName));

    this.master.getMongoDBConnector().getMongoDatabase().getCollection("groups").insertOne(groupDB.create());
    this.groups.put(groupDB.getGroupName(), groupDB);
    this.master.sendMessage("create default 'lobby'");
  }

  private SubGroupDB createSubGroup(String name) {
    SubGroupDB subGroupDB = new SubGroupDB(null);
    subGroupDB.setSubGroupName(name);

    ServerDB serverDB = new ServerDB();

    serverDB.setDisplayName("§eLobby§7-§e");
    serverDB.setColor("§e");
    serverDB.setWeightClass(10);
    serverDB.setMaxOnlineAmount(999);
    serverDB.setMinOnlineAmount(2);
    serverDB.setMaxPlayer(50);
    serverDB.setStartServerByPlayersLimit(50);
    serverDB.setMaintenance(false);
    serverDB.setGlobalCheck(false);

    subGroupDB.setServerDB(serverDB);
    return subGroupDB;
  }
}
