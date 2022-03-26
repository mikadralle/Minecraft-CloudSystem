package de.leantwi.cloudsystem.api.database;

import redis.clients.jedis.JedisPool;

public interface IRedis {

    void connect();
    void disconnect();
    JedisPool getJedisPool();

}
