package de.leantwi.cloudsystem.master.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import de.leantwi.cloudsystem.master.MasterBootstrap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;

@Getter
public class MongoDBConnector {

  private final MasterBootstrap master = MasterBootstrap.getInstance();
  private MongoClient mongoClient;
  private MongoDatabase mongoDatabase;

  public void connect() {

    final String mongo_host = master.getConfigAPI().getProperty("mongoDB.host");
    final String mongo_auth_db = master.getConfigAPI().getProperty("mongoDB.authDB");
    final String mongo_default_db = master.getConfigAPI().getProperty("mongoDB.defaultDB");
    final String mongo_user = master.getConfigAPI().getProperty("mongoDB.user");
    final String mongo_pass = master.getConfigAPI().getProperty("mongoDB.password");

    Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
    final MongoCredential credential = MongoCredential.createCredential(mongo_user, mongo_auth_db, mongo_pass.toCharArray());
    final MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).connectionsPerHost(2000).build();
    mongoClient = new MongoClient(new ServerAddress(mongo_host), credential, options);
    mongoDatabase = mongoClient.getDatabase(mongo_default_db);
    this.master.sendMessage("MongoDB-Connector is connected");
  }

  public void disconnect() {
    mongoClient.close();
    this.master.sendMessage("MongoDB-Connector is disconnected.");
  }

  public MongoClient getMongoClient() {
    return mongoClient;
  }

}
