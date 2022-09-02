package de.leantwi.cloudsystem.proxy.command;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudPlayer;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.events.gameserver.StopGameServerEvent;
import de.leantwi.cloudsystem.api.events.master.RequestsServerEvent;
import de.leantwi.cloudsystem.api.gameserver.groups.SubGroupDB;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Optional;

public class CloudCommand extends Command {
    @Getter
    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();

    public CloudCommand() {
        super("cloud", "cloud.use");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        if (!commandSender.hasPermission("cloud.use")) {
            return;
        }

        if (strings.length == 1) {

            if (strings[0].equalsIgnoreCase("stop")) {
                ProxiedPlayer player = (ProxiedPlayer) commandSender;
                CloudPlayer cloudPlayer = CloudPlayer.getCloudPlayer(player.getUniqueId());
                CloudSystem.getEventAPI().callEvent(new StopGameServerEvent(cloudPlayer.getGameServerName()));
                return;
            }
            if (strings[0].equalsIgnoreCase("info")) {

                ProxiedPlayer player = (ProxiedPlayer) commandSender;
                CloudPlayer cloudPlayer = CloudPlayer.getCloudPlayer(player.getUniqueId());
                cloudPlayer.sendMessage("Du bist auf " + cloudPlayer.getGameServerName() + " online.");
                return;
            }
            commandSender.sendMessage("§c/cloud stop : You will be stopping the current GameServer");
            commandSender.sendMessage("§c/cloud info : You see all Server infos");
            return;
        }

        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("info")) {

                ProxiedPlayer player = (ProxiedPlayer) commandSender;
                CloudPlayer cloudPlayer = CloudPlayer.getCloudPlayer(player.getUniqueId());
                CloudPlayer targetCloudPlayer = CloudPlayer.getCloudPlayer(strings[1].toLowerCase());
                if (targetCloudPlayer != null) {
                    cloudPlayer.sendMessage("Der Spieler " + targetCloudPlayer.getPlayerName() + " ist auf " + targetCloudPlayer.getGameServerName() + " online.");
                    return;
                }
                cloudPlayer.sendMessage("Der Spieler " + strings[1] + " ist nicht online!");
                return;
            }
        }


        if (strings.length == 3) {

            if (strings[0].equalsIgnoreCase("send")) {

                String targetServer = strings[2];

                if (strings[1].equalsIgnoreCase("all")) {

                    CloudSystem.getAPI().getAllCloudPlayers().forEach(cloudPlayers -> cloudPlayers.connect(targetServer));


                    return;
                }

            }

            if (strings[0].equalsIgnoreCase("start")) {


                Optional<SubGroupDB> subGroupDB = this.cloudSystemAPI.getSubGroupByName(strings[1].toLowerCase());

                if (subGroupDB.isPresent()) {
                    int amount = Integer.parseInt(strings[2]);

                    CloudSystem.getEventAPI().callEvent(new RequestsServerEvent(subGroupDB.get().getSubGroupName(), amount));
                    commandSender.sendMessage(ProxyConnector.getInstance().getCloudPrefix() + "The cloud will be starting §e " + amount + " §7gameservers.");
                    return;
                }


                commandSender.sendMessage(ProxyConnector.getInstance().getCloudPrefix() + "§cThe group §e" + strings[1].toLowerCase() + "§c could not found!");
                return;
            }
            commandSender.sendMessage("§c/cloud start <SubGroupName> <Amount>");

        }

    }

    //cloud start <Group-Name> <Amount>
    //cloud start <SubGroup-Group> <Amount>
    //cloud stop <Server-Name> // cloud stop
    //cloud info -> show all server info
    //cloud send <Spieler/all> <target Server>
    //cloud info <Name>
    //cloud notify
}
