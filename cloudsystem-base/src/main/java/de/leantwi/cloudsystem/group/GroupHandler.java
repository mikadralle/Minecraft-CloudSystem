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

    public GroupHandler(MongoDBConnectorAPI mongoDB) {
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

        GroupDB groupDB = this.groups.get(groupName);


        groupDB.getSubGroupDBList().add(createSubGroup(groupName, this.groups.get(groupName)));
        this.mongoDB.getMongoDatabase().getCollection("groups").updateOne(new Document("groupName", groupName), new Document("$set", groupDB.create()));
        this.groups.put(groupDB.getGroupName(), groupDB);
    }

    private void createDefault(String groupName, String subGroupName) {

        GroupDB groupDB = new GroupDB();
        groupDB.setGroupName(groupName);

        groupDB.getSubGroupDBList().add(createSubGroup(subGroupName, groupDB));
        this.mongoDB.getMongoDatabase().getCollection("groups").insertOne(groupDB.create());

        groupDB.fetch(groupDB.create());

        this.groups.put(groupDB.getGroupName(), groupDB);
        System.out.println("create default 'lobby'");
    }

    public void addSubGroupToGroup(String groupName, String subGroupName) {

        GroupDB groupDB = this.groups.get(groupName);
        groupDB.getSubGroupDBList().add(createSubGroup(subGroupName, groupDB));
        this.mongoDB.getMongoDatabase().getCollection("groups").insertOne(groupDB.create());
        groupDB.fetch(groupDB.create());
        this.groups.put(groupDB.getGroupName(), groupDB);

    }

    private SubGroupDB createSubGroup(String subGroupName, GroupDB groupDB) {
        SubGroupDB subGroupDB = new SubGroupDB(groupDB);
        System.out.println("SubGroupName: " + subGroupName);
        subGroupDB.setSubGroupName(subGroupName);

        ServerDB serverDB = new ServerDB();

        serverDB.setDisplayName("Default-Server");
        serverDB.setColor("§e");
        serverDB.setMemory(1024);
        serverDB.setMaxOnlineAmount(999);
        serverDB.setMinOnlineAmount(1);
        serverDB.setMaxPlayer(50);
        serverDB.setStartServerByPlayersLimit(50);
        serverDB.setMaintenance(false);
        serverDB.setGlobalCheck(false);

        subGroupDB.setServerDB(serverDB);

        return subGroupDB;
    }
}
