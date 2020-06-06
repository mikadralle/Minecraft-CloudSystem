package eu.unyfy.master.database.redis;

import eu.unyfy.master.MasterBootstrap;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

@Getter
public class RedisConnector {

  private final MasterBootstrap master = MasterBootstrap.getInstance();
  //
  private JedisPool jedisPool;

  public void connect() {

    master.sendMessage("redis will be connected");
    FutureTask<JedisPool> task = new FutureTask<>(new Callable<JedisPool>() {
      @Override
      public JedisPool call() throws Exception {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(80);
        return new JedisPool(jedisPoolConfig,
            master.getConfigAPI().getProperty("redis.host"),
            Integer.parseInt(master.getConfigAPI().getProperty("redis.port")),
            2000,
            master.getConfigAPI().getProperty("redis.password"),
            Integer.parseInt(master.getConfigAPI().getProperty("redis.databaseID")));
      }
    });

    this.master.getExecutorService().execute(task);

    try {
      this.jedisPool = task.get();
    } catch (InterruptedException | ExecutionException e) {
    }

    this.master.getBootstrapConsole().info("Redis has been connected successful");
  }

  public void disconnect() {
    this.jedisPool.destroy();
  }


  private void clearCache() {
    try (Jedis jedis = this.jedisPool.getResource()) {
      final Set<String> list = jedis.keys("cloud:*");
      final Pipeline pipeline = jedis.pipelined();
      list.forEach(pipeline::del);
      pipeline.sync();
    }
  }

}
