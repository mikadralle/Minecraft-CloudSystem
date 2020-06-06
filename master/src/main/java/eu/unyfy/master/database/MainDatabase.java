package eu.unyfy.master.database;

import eu.unyfy.master.database.mongo.DatabaseHandler;
import eu.unyfy.master.database.mongo.MongoDBConnector;
import eu.unyfy.master.database.redis.RedisConnector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MainDatabase {

  private final RedisConnector redisConnector;
  private final MongoDBConnector mongoDBConnector;
  private final DatabaseHandler databaseHandler;

}
