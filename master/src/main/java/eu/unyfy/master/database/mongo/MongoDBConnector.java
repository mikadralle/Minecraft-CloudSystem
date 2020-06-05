package eu.unyfy.master.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import eu.unyfy.master.Master;
import eu.unyfy.master.api.config.ConfigAPI;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;

@Getter
public class MongoDBConnector {

  private final Master master = Master.getInstance();
  private final ConfigAPI configAPI = getMaster().getConfigAPI();
  private MongoClient mongoClient;
  private MongoDatabase mongoDatabase;

  public void connect() {

    final String mongo_host = configAPI.getConfigAPI().getProperty("mongoDB.host");
    final String mongo_auth_db = configAPI.getConfigAPI().getProperty("mongoDB.authDB");
    final String mongo_default_db = configAPI.getConfigAPI().getProperty("mongoDB.defaultDB");
    final String mongo_user = configAPI.getConfigAPI().getProperty("mongoDB.user");
    final String mongo_pass = configAPI.getConfigAPI().getProperty("mongoDB.password");

    Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
    final MongoCredential credential = MongoCredential.createCredential(mongo_user, mongo_auth_db, mongo_pass.toCharArray());
    final MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).connectionsPerHost(2000).build();
    mongoClient = new MongoClient(new ServerAddress(mongo_host), credential, options);
    mongoDatabase = mongoClient.getDatabase(mongo_default_db);
    Master.getInstance().getConsole().sendMessage("MongoDB-Connector is connected");
  }

  public void disconnect() {
    mongoClient.close();
    Master.getInstance().getConsole().sendMessage("MongoDB-Connector is disconnected.");
  }

  public MongoClient getMongoClient() {
    return mongoClient;
  }

}
