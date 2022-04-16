package de.leantwi.cloudsystem.master.command;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemBase;
import de.leantwi.cloudsystem.api.gameserver.groups.GroupDB;
import de.leantwi.cloudsystem.api.gameserver.groups.SubGroupDB;
import de.leantwi.cloudsystem.group.GroupHandler;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import de.leantwi.service.command.CommandImplementation;

import java.util.Collection;

public class GroupCommand implements CommandImplementation {
    @Override
    public void execute(String[] strings) {


        //group create <Group-Name> <Subgroup-Name>
        //group addSubGroup <Group-Name> <SubGroup-Name>


        if (strings.length == 3) {
            if (strings[0].equalsIgnoreCase("create")) {

                String groupName = strings[1];
                String subGroupName = strings[2];
                Collection<GroupDB> list = CloudSystem.getAPI().getAllGroups();

                if (!list.stream().anyMatch(groupDB -> groupDB.getGroupName().equalsIgnoreCase(groupName))) {

                    GroupHandler.getInstance().createGroup(groupName, subGroupName);
                    MasterBootstrap.getInstance().getLogger().info("The Group " + groupName + " has been created. §cPlease restart the system!");
                    return;
                }
                MasterBootstrap.getInstance().getLogger().info("§cThe Group " + groupName + " exists already!");
                return;

            }

            if(strings[0].equalsIgnoreCase("addsubgroup")){

                String groupName = strings[1];
                String subGroupName = strings[2];
                Collection<GroupDB> list = CloudSystem.getAPI().getAllGroups();
                Collection<SubGroupDB> subGroupDBS = CloudSystem.getAPI().getAllSubGroups();

                if (list.stream().anyMatch(groupDB -> groupDB.getGroupName().equalsIgnoreCase(groupName)) && subGroupDBS.stream().anyMatch(subGroupDB -> subGroupDB.getSubGroupName().equalsIgnoreCase(subGroupName))) {

                    GroupHandler.getInstance().addSubGroupToGroup(groupName, subGroupName);
                    MasterBootstrap.getInstance().getLogger().info("The SubGroup " + subGroupName + " has been added to the main group " +  groupName + ". §cPlease restart the system!");
                    return;
                }
                MasterBootstrap.getInstance().getLogger().info("§cThe group " + groupName + " or the subgroup " +subGroupName+ " does not exists!");
                return;


            }



        }
        MasterBootstrap.getInstance().getLogger().info("use the command /group create <group-name> <subgroup-name>");
        MasterBootstrap.getInstance().getLogger().info("use the command /group addsubgroup <group-name> <subgroup-name>");

    }

    @Override
    public String getName() {
        return "groups";
    }

    @Override
    public boolean isUsageRight(String[] strings) {
        return true;
    }

    @Override
    public String getUsage() {
        return "create a group, manage a group or delete a group";
    }
}
