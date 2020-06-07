package eu.unyfy.wrapper.database.redis;

import eu.unyfy.wrapper.WrapperBootstrap;
import eu.unyfy.wrapper.utils.Cache;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import lombok.Getter;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Getter
public class RedisConnector {

  private final WrapperBootstrap wrapper = WrapperBootstrap.getInstance();
  private JedisPool jedisPool;

  public void connect() {
    Cache.sendMessage("redis will be connected");

    FutureTask<JedisPool> task = new FutureTask<>(new Callable<JedisPool>() {
      @Override
      public JedisPool call() throws Exception {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(80);
        return new JedisPool(jedisPoolConfig, wrapper.getConfigAPI().getProperty("redis.hostname"), Integer.parseInt(wrapper.getConfigAPI().getProperty("redis.port")), 2000, wrapper.getConfigAPI().getProperty("redis.password"),
            Integer.parseInt(wrapper.getConfigAPI().getProperty("redis.databaseID")));
      }
    });
    this.wrapper.getExecutorService().execute(task);
    try {
      this.jedisPool = task.get();
      Cache.sendMessage("redis has been connected.");
    } catch (InterruptedException | ExecutionException e) {
      Cache.sendMessage("Redis-Server Status:");
      Cache.sendMessage("Fehlgeschlagen! Fehler: \n" + e.getMessage());
    }
  }

  public void disconnect() {

    this.jedisPool.destroy();


  }

}
