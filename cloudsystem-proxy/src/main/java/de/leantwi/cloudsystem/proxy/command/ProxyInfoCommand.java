package de.leantwi.cloudsystem.proxy.command;

import de.leantwi.cloudsystem.api.CloudPlayer;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ProxyInfoCommand extends Command {

    private final String proxyID;
    private final String prefix;

    public ProxyInfoCommand() {
        super("proxy", "cloud.command.proxy", "proxyInfo", "bungeeID");
        this.proxyID = ProxyConnector.getInstance().getCloudProxy().getProxyID();
        this.prefix = ProxyConnector.getInstance().getCloudPrefix();
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(this.prefix + "Du bist auf §e" + proxyID + " §7online.");
        } else if (strings.length == 1) {

            if (!commandSender.hasPermission("cloud.command.proxyID")) {
                commandSender.sendMessage(this.prefix + "Du bist auf §e" + proxyID + " §7online.");
                return;
            }

            CloudPlayer cloudPlayer = CloudPlayer.getCloudPlayer(strings[0].toLowerCase());
            if (cloudPlayer == null) {
                commandSender.sendMessage(this.prefix + "Der Spieler ist nicht online!");
                return;
            }

            commandSender.sendMessage(this.prefix + "Der Spieler §e" + cloudPlayer.getPlayerName() + "§7 ist auf dem §e" + cloudPlayer.getProxyID() + " §7online. ");

        }
    }
}
