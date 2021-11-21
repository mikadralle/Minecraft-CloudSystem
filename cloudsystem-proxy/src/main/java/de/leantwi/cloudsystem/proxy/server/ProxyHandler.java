package de.leantwi.cloudsystem.proxy.server;

import de.leantwi.cloudsystem.proxy.ProxyConnector;
import de.leantwi.cloudsystem.proxy.database.NatsConnector;
import de.leantwi.cloudsystem.proxy.database.RedisConnector;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ProxyHandler {
    private final ProxyConnector proxyConnector = ProxyConnector.getInstance();
    private final RedisConnector redisConnector = this.proxyConnector.getRedisConnector();
    private final NatsConnector natsConnector =  this.proxyConnector.getNatsConnector();
    private final String prefix = ProxyConnector.getInstance().getCloudPrefix();
    @Getter
    private String bungeeName = "bungeecord-99";

    public void loginProxyServer() {
        try {
            this.bungeeName = this.natsConnector.request("verify", "bungeecord_register#");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        this.addBungeeCord(this.bungeeName);
        this.loadOnlineServer();
    }

    public void logoutProxyServer() {
        removeBungeeCord();
        this.natsConnector.publishMessage("cloud", "logout_bungeecord#" + this.bungeeName);
        ProxyServer.getInstance().getPlayers().forEach(players -> players.disconnect(this.prefix + "§cProxy will be restarted!"));
    }

    protected void addServer(String serverName, String address, int port) {
        if (ProxyServer.getInstance().getServers().containsKey(serverName)) {
            removeServer(serverName);
        }
        ProxyServer.getInstance().getServers().put(serverName, ProxyServer.getInstance().constructServerInfo(serverName, InetSocketAddress.createUnresolved(address, port), "-", false));
        ProxyServer.getInstance().getPlayers().stream().filter(players -> players.hasPermission("cloud.use")).forEach(players -> players.sendMessage(this.prefix + "The server " + serverName + " §7is now §aonline§7."));
        ProxyServer.getInstance().getConsole().sendMessage(this.prefix + "The server " + serverName + " §7is now §aonline§7.");
    }

    protected void removeServer(String serverName) {

        ServerInfo kickTo = ProxyConnector.getInstance().getBungeeConnector().getLobbyServer();
        ProxyServer.getInstance().getServerInfo(serverName).getPlayers().forEach(players -> players.connect(kickTo));
        ProxyServer.getInstance().getServers().remove(serverName);
        ProxyServer.getInstance().getPlayers().stream().filter(players -> players.hasPermission("cloud.use")).forEach(players -> players.sendMessage(this.prefix + "The server " + serverName + " §7is now §coffline§7."));
        ProxyServer.getInstance().getConsole().sendMessage(this.prefix + "The server " + serverName + " §7is now §coffline§7.");
    }

    private void loadOnlineServer() {
        try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
            jedis.select(7);
            final Set<String> list = jedis.keys("cloud:sessions:serverlist:*");
            list.forEach(key -> ProxyConnector.getInstance().getBungeeConnector().createServer(key.split(":")[3]));
        }
    }

    private void addBungeeCord(String bungeeName) {
        try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
            jedis.select(3);
            jedis.hset("multiproxy.info", bungeeName, String.valueOf(System.currentTimeMillis()));
        }
    }

    private void removeBungeeCord() {
        try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
            jedis.select(3);
            jedis.hdel("multiproxy.info", getBungeeName());
        }
    }

    public Set<String> getBungeeCordList() {

        try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
            jedis.select(3);
            return jedis.hgetAll("multiproxy.info").keySet();
        }

    }

    public void shutdownBungeeCord(String bungeeName) {
        natsConnector.publishMessage("backend", "stop#" + bungeeName.toLowerCase());
    }

    public void shutdownAllBungeeCord() {
        natsConnector.publishMessage("backend", "stop_all");
    }


}