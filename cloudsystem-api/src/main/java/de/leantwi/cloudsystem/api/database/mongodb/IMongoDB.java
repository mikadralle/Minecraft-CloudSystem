package de.leantwi.cloudsystem.api.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public interface IMongoDB {


    void connect();
    void disconnect();
    MongoClient getMongoClient();
    MongoDatabase getMongoDatabase();
}
