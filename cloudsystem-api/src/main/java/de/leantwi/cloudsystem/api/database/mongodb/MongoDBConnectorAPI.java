package de.leantwi.cloudsystem.api.database.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public interface MongoDBConnectorAPI {


    void connect();
    void disconnect();
    MongoClient getMongoClient();
    MongoDatabase getMongoDatabase();
}
