package de.leantwi.cloudsystem.database;

import de.leantwi.cloudsystem.api.database.IRedis;
import lombok.RequiredArgsConstructor;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@RequiredArgsConstructor
public class RedisConnector implements IRedis {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private JedisPool jedisPool;
    private final String hostname, password;
    private final int port, databaseID;

    @Override
    public void connect() {

        FutureTask<JedisPool> task = new FutureTask<>(() -> {

            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(80);
            return new JedisPool(jedisPoolConfig,
                    this.hostname,
                    this.port,
                    2000,
                    this.password,
                    this.databaseID);
        });

        this.executorService.execute(task);

        try {
            this.jedisPool = task.get();
            System.out.println("Redis has been connected");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void disconnect() {
        this.jedisPool.destroy();

    }

    @Override
    public JedisPool getJedisPool() {
        return this.jedisPool;
    }
}
