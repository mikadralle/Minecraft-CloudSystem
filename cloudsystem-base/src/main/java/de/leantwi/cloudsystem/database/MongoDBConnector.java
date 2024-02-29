package de.leantwi.cloudsystem.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SslSettings;
import de.leantwi.cloudsystem.api.database.mongodb.MongoDBConnectorAPI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@RequiredArgsConstructor
public class MongoDBConnector implements MongoDBConnectorAPI {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private final String hostName, authDB, defaultDB, user, password;

    public void connect() {

        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        final MongoCredential credential = MongoCredential.createCredential(user, authDB, password.toCharArray());
        final SslSettings sslSettings = SslSettings.builder().enabled(false).build();
        final ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder().maxSize(2000).build();
        final ClusterSettings clusterSettings = ClusterSettings.builder().hosts(List.of(new ServerAddress(hostName))).build();
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .credential(credential)
                .applyToSslSettings(builder -> builder.applySettings(sslSettings))
                .applyToClusterSettings(builder -> builder.applySettings(clusterSettings))
                .applyToConnectionPoolSettings(builder -> builder.applySettings(connectionPoolSettings))
                .build();
        mongoClient = MongoClients.create(mongoClientSettings);
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

    public MongoDatabase getMongoDatabase() {
        return this.mongoDatabase;
    }

}
