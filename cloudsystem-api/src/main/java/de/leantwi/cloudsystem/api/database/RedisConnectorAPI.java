package de.leantwi.cloudsystem.api.database;

import redis.clients.jedis.JedisPool;

public interface RedisConnectorAPI {

    void connect();
    void disconnect();
    JedisPool getJedisPool();

}
