package eu.unyfy.master.database;

import eu.unyfy.master.Master;
import eu.unyfy.master.database.mongo.DatabaseHandler;
import eu.unyfy.master.database.mongo.MongoDBConnector;
import eu.unyfy.master.database.redis.RedisConnector;
import lombok.Getter;

@Getter
public class MainDatabase {

  private final Master master = Master.getInstance();
  private final RedisConnector redisConnector = master.getRedisConnector();
  private final MongoDBConnector mongoDBConnector = master.getMongoDBConnector();

  private final DatabaseHandler databaseHandler = this.master.getDatabaseHandler();

}
