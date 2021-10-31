package de.leantwi.cloudsystem.bukkit.server;

import de.leantwi.cloudsystem.bukkit.BukkitConnector;
import de.leantwi.cloudsystem.bukkit.config.IniFile;
import de.leantwi.cloudsystem.bukkit.database.NatsConnector;
import de.leantwi.cloudsystem.bukkit.database.RedisConnector;
import lombok.Getter;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class SpigotConnector {

    private final NatsConnector natsConnector = BukkitConnector.getInstance().getNatsConnector();
    private final RedisConnector redisConnector = BukkitConnector.getInstance().getRedisConnector();
    //
    private final Map<String, ServerFactory> serverFactoryMap = new HashMap<>();

    private String serverName;

    public ServerFactory getServerFactory(String serverName) {
        return this.serverFactoryMap.get(serverName);
    }

    public void registerServer(String serverName) {
        if (this.serverFactoryMap.containsKey(serverName)) {
            return;
        }
        ServerFactory serverFactory = new ServerFactory(serverName);
        serverFactory.fetch();
        this.serverFactoryMap.put(serverName, serverFactory);
        Bukkit.getConsoleSender().sendMessage("The server " + serverName + " will be registered ");
    }

    public void unRegisterServer(String serverName) {
        this.serverFactoryMap.remove(serverName);
        Bukkit.getConsoleSender().sendMessage("The server " + serverName + " will be registered ");
    }

    public void loginSpigotServer() {
        final IniFile iniFile = new IniFile("cloud.ini");
        this.serverName = iniFile.getProperty("serverName");
        this.natsConnector.publishMessage("cloud", "online#" + this.serverName);
        this.registerServer(this.serverName);
        BukkitConnector.getInstance().getExecutorService().execute(this::loadAllServer);

        Bukkit.getConsoleSender().sendMessage("§aSlots: " + getServerFactoryMap().get(this.serverName).getSessionServer().getSlots());
    }

    public void logoutSpigotServer() {
        this.natsConnector.publishMessage("cloud", "offline#" + this.serverName);
    }

    private void loadAllServer() {
        try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
            jedis.select(8);
            final Set<String> list = jedis.keys("cloud:sessions:serverlist:*");
            list.forEach(key -> this.registerServer(key.split(":")[3]));
        }
    }


}
