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

        this.groups.clear();

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


        groupDB.getSubGroupDBList().add(createSubGroup(groupName, groupName));
        this.mongoDB.getMongoDatabase().getCollection("groups").updateOne(new Document("groupName", groupName), new Document("$set", groupDB.create()));
        this.groups.put(groupDB.getGroupName(), groupDB);
    }

    private void createDefault(String groupName, String subGroupName) {

        GroupDB groupDB = new GroupDB();
        groupDB.setGroupName(groupName);

        groupDB.getSubGroupDBList().add(createSubGroup(subGroupName, groupName));
        this.mongoDB.getMongoDatabase().getCollection("groups").insertOne(groupDB.create());
        groupDB.fetch(groupDB.create());
        this.groups.put(groupDB.getGroupName(), groupDB);
        System.out.println("create default 'lobby'");
    }

    /**
     * create a group with a head main group
     *
     * @param mainGroupName the main group name
     * @param subGroupName  the sub group name
     */
    public void addSubGroupToGroup(String mainGroupName, String subGroupName) {
        GroupDB groupDB;
        if (!this.groups.containsKey(mainGroupName)) {
            groupDB = new GroupDB();
            groupDB.setGroupName(mainGroupName);
            System.out.println("create main group " + mainGroupName);

        } else {
            groupDB = this.groups.get(mainGroupName);
        }
        //create subgroup
        groupDB.getSubGroupDBList().add(createSubGroup(subGroupName, groupDB.getGroupName()));
        //insert into the database
        if (this.groups.containsKey(mainGroupName)) {
            this.mongoDB.getMongoDatabase().getCollection("groups").updateOne(new Document("groupName", groupDB.getGroupName()), new Document("$set", groupDB.create()));
        } else {
            this.mongoDB.getMongoDatabase().getCollection("groups").insertOne(groupDB.create());
        }
        //put in the hashmap
        this.groups.put(groupDB.getGroupName(), groupDB);

    }

    private SubGroupDB createSubGroup(String subGroupName, String mainGroupName) {
        SubGroupDB subGroupDB = new SubGroupDB(mainGroupName);
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
