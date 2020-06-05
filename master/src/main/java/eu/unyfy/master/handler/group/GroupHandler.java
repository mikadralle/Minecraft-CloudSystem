package eu.unyfy.master.handler.group;

import eu.unyfy.master.Master;
import eu.unyfy.master.database.MainDatabase;
import eu.unyfy.master.database.group.GroupDB;
import eu.unyfy.master.database.group.ServerDB;
import eu.unyfy.master.database.group.SubGroupDB;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

@Getter
@Setter
public class GroupHandler {

  private Map<String, GroupDB> groups = new HashMap<>();

  private Master master = Master.getInstance();
  private MainDatabase mainDatabase = this.master.getMainDatabase();

  public void fetch() {

    this.mainDatabase.getDatabaseHandler().getCollection("groups").find().iterator().forEachRemaining(document -> {
      GroupDB groupDB = new GroupDB();
      groupDB.fetch(document);
      this.groups.put(groupDB.getGroupName(), groupDB);
      System.out.println("groupName:" + groupDB.getGroupName());
    });

    if (!this.groups.isEmpty()) {
      Master.getInstance().getConsole().sendMessage("Groups is loaded susses.");
      return;
    }

    Master.getInstance().getConsole().sendMessage("couldn't find group! create a new group");
    createDefault("lobby", "lobby");

  }

  public void createGroup(String groupName, String subGroupName) {

    if (!this.groups.containsKey(groupName)) {
      createDefault(groupName, subGroupName);
      return;
    }

    this.groups.get(groupName).getSubGroupDBList().add(createSubGroup(subGroupName));
    this.mainDatabase.getDatabaseHandler().getCollection("groups").updateOne(new Document("groupName", groupName), new Document("$set", groups.get(groupName).create()));
    this.groups.put(groups.get(groupName).getGroupName(), groups.get(groupName));

  }

  private void createDefault(String groupName, String subGroupName) {

    GroupDB groupDB = new GroupDB();
    groupDB.setGroupName(groupName);

    groupDB.getSubGroupDBList().add(createSubGroup(subGroupName));

    this.mainDatabase.getDatabaseHandler().getCollection("groups").insertOne(groupDB.create());
    this.groups.put(groupDB.getGroupName(), groupDB);
    Master.getInstance().getConsole().sendMessage("create default 'lobby'");
  }

  private SubGroupDB createSubGroup(String name) {
    SubGroupDB subGroupDB = new SubGroupDB(null);
    subGroupDB.setSubGroupName(name);

    ServerDB serverDB = new ServerDB();

    serverDB.setDisplayName("§eLobby§7-§e");
    serverDB.setColor("§e");
    serverDB.setMemory(512);
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
