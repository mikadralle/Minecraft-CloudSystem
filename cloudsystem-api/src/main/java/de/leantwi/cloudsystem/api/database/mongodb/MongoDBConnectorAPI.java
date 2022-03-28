package de.leantwi.cloudsystem.api.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public interface MongoDBConnectorAPI {


    void connect();
    void disconnect();
    MongoClient getMongoClient();
    MongoDatabase getMongoDatabase();
}
