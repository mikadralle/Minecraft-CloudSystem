package de.leantwi.cloudsystem.group;

import de.leantwi.cloudsystem.api.database.mongodb.MongoDBConnectorAPI;
import de.leantwi.cloudsystem.api.gameserver.groups.GroupDB;
import de.leantwi.cloudsystem.api.gameserver.groups.SubGroupDB;
import de.leantwi.cloudsystem.api.gameserver.server.ServerDB;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class GroupHandler {

    public MongoDBConnectorAPI mongoDB;
    private Map<String, GroupDB> groups = new HashMap<>();

    @Getter
    private static GroupHandler instance;

    public GroupHandler(MongoDBConnectorAPI mongoDB){
        this.mongoDB = mongoDB;
        instance = this;

    }


    public void fetch() {

        this.mongoDB.getMongoDatabase().getCollection("groups").find().iterator().forEachRemaining(document -> {
            GroupDB groupDB = new GroupDB();
            groupDB.fetch(document);
            this.groups.put(groupDB.getGroupName(), groupDB);
        });

        if (!this.groups.isEmpty()) {
            System.out.println("Groups is loaded susses.");
            return;
        }

        System.out.println("couldn't find group! create a new group");
        createDefault("lobby", "lobby");

    }

    public void createGroup(String groupName) {

        if (!this.groups.containsKey(groupName)) {
            createDefault(groupName, groupName);
            return;
        }

        this.groups.get(groupName).getSubGroupDBList().add(createSubGroup(groupName));
        this.mongoDB.getMongoDatabase().getCollection("groups").updateOne(new Document("groupName", groupName), new Document("$set", groups.get(groupName).create()));
        this.groups.put(groups.get(groupName).getGroupName(), groups.get(groupName));

    }

    private void createDefault(String groupName, String subGroupName) {

        GroupDB groupDB = new GroupDB();
        groupDB.setGroupName(groupName);

        groupDB.getSubGroupDBList().add(createSubGroup(subGroupName));

        this.mongoDB.getMongoDatabase().getCollection("groups").insertOne(groupDB.create());
        this.groups.put(groupDB.getGroupName(), groupDB);
        System.out.println("create default 'lobby'");
    }

    public void addSubGroupToGroup(String groupName, String subGroupName) {

        GroupDB groupDB = this.groups.get(groupName);
        groupDB.getSubGroupDBList().add(createSubGroup(subGroupName));
        this.mongoDB.getMongoDatabase().getCollection("groups").insertOne(groupDB.create());
        this.groups.put(groupDB.getGroupName(), groupDB);

    }

    private SubGroupDB createSubGroup(String name) {
        SubGroupDB subGroupDB = new SubGroupDB(null);
        subGroupDB.setSubGroupName(name);

        ServerDB serverDB = new ServerDB();

        serverDB.setDisplayName("§eLobby§7-§e");
        serverDB.setColor("§e");
        serverDB.setMemory(1024);
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
