package de.leantwi.cloudsystem.proxy.database;


import de.leantwi.cloudsystem.proxy.ProxyConnector;
import de.leantwi.cloudsystem.proxy.config.IniFile;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@Getter
public class RedisConnector {


    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final IniFile configAPI = ProxyConnector.getInstance().getConfigAPI();
    private JedisPool jedisPool;

    public void connect() {

        FutureTask<JedisPool> task = new FutureTask<JedisPool>(() -> {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(80);
            return new JedisPool(jedisPoolConfig, configAPI.getProperty("redis.host"), Integer.parseInt(configAPI.getProperty("redis.port")), 2000, configAPI.getProperty("redis.password"), Integer.parseInt(configAPI.getProperty("redis.databaseID")));
        });

        this.executorService.submit(task);
        try {
            this.jedisPool = task.get();
            ProxyServer.getInstance().getConsole().sendMessage("the redis client has been connected to database.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.hset("modulary.lentry.logins", String.valueOf(System.currentTimeMillis()), "login susses");
        }
    }

    public void disconnect() {

        this.jedisPool.destroy();
        ProxyServer.getInstance().getConsole().sendMessage("the redis client has been disconnected");

    }
}
