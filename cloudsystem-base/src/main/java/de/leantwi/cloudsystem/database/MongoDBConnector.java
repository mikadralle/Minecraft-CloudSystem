package de.leantwi.cloudsystem.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import de.leantwi.cloudsystem.api.database.mongodb.IMongoDB;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@RequiredArgsConstructor
public class MongoDBConnector implements IMongoDB {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private final String hostName, authDB, defaultDB, user, password;

    public void connect() {

        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        final MongoCredential credential = MongoCredential.createCredential(user, authDB, password.toCharArray());
        final MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).connectionsPerHost(2000).build();
        mongoClient = new MongoClient(new ServerAddress(hostName), credential, options);
        mongoDatabase = mongoClient.getDatabase(defaultDB);
        System.out.println("The MongoDB client has been connected.");
    }

    public void disconnect() {
        System.out.println("The MongoDB client has been disconnected.");
        mongoClient.close();
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase(){
        return this.mongoDatabase;
    }

}
