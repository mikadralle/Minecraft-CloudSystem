package eu.unyfy.master.database.redis;

import eu.unyfy.master.Master;
import eu.unyfy.master.api.config.ConfigAPI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import lombok.Getter;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Getter
public class RedisConnector {

  private final Master master = Master.getInstance();
  private final ConfigAPI configAPI = this.master.getConfigAPI();
  //
  private JedisPool jedisPool;

  public void connect() {
    Master.getInstance().getConsole().sendMessage("Verbinde mit Redis-Database...");

    FutureTask<JedisPool> task = new FutureTask<>(new Callable<JedisPool>() {
      @Override
      public JedisPool call() throws Exception {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(80);
        return new JedisPool(jedisPoolConfig,
            configAPI.getConfigAPI().getProperty("redis.host"),
            Integer.parseInt(configAPI.getConfigAPI().getProperty("redis.port")),
            2000,
            configAPI.getConfigAPI().getProperty("redis.password"),
            Integer.parseInt(configAPI.getConfigAPI().getProperty("redis.databaseID")));
      }
    });

    this.master.getExecutorService().execute(task);

    try {
      this.jedisPool = task.get();
    } catch (InterruptedException | ExecutionException e) {
      Master.getInstance().getConsole().sendMessage("Redis-Server Status:");
      Master.getInstance().getConsole().sendMessage("Fehlgeschlagen! Fehler: \n" + e.getMessage());
    }
    Master.getInstance().getConsole().sendMessage("Redis-Server Status:");
    Master.getInstance().getConsole().sendMessage("Connected");
  }

  public void disconnect() {
    this.jedisPool.destroy();
  }


}
