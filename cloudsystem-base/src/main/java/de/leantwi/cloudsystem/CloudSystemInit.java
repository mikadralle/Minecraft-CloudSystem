package de.leantwi.cloudsystem;

import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.CloudSystemBase;
import de.leantwi.cloudsystem.database.MongoDBConnector;
import de.leantwi.cloudsystem.database.NatsConnector;
import de.leantwi.cloudsystem.database.RedisConnector;
import de.leantwi.cloudsystem.event.EventHandler;

import java.util.Properties;

public class CloudSystemInit {


    public CloudSystemInit() {

        //SET PROPERTIES
        Properties properties = System.getProperties();
        //Nats
        properties.setProperty("nats.hostname", "nats.leantwi.de");
        properties.setProperty("nats.token", "C1adWuKDBMR8WXwgTTyLeyKebmVJcmwY");
        properties.setProperty("nats.port", "4222");
        //Redis
        properties.setProperty("redis.hostname", "redis.leantwi.de");
        properties.setProperty("redis.password", "n2A0c8O707dLErT2BhySfyY59C8dhb");
        properties.setProperty("redis.port", "6379");
        properties.setProperty("redis.databaseID", "7");

        properties.setProperty("mongoDB.hostname", "mongodb.leantwi.de");
        properties.setProperty("mongoDB.authDB", "admin");
        properties.setProperty("mongoDB.defaultDB", "cloud");
        properties.setProperty("mongoDB.user", "revane");
        properties.setProperty("mongoDB.password", "GEpC4bsvXnTywNggc3DnZvL6RWaP3unN6LcnNzNpemwaFSKkVkf1Z7um2K6Y");

        //AirBerlin hat kein Plan vom Linux
        CloudSystemAPI cloudSystemAPI = new CloudSystemBase(new NatsConnector(
                System.getProperty("nats.hostname"),
                System.getProperty("nats.token"), 4222),
                new RedisConnector(System.getProperty("redis.hostname"), System.getProperty("redis.password"),
                        Integer.parseInt(System.getProperty("redis.port")),
                        Integer.parseInt(System.getProperty("redis.databaseID"))),
                new MongoDBConnector(System.getProperty("mongoDB.hostname"),
                        System.getProperty("mongoDB.authDB"),
                        System.getProperty("mongoDB.defaultDB"),
                        System.getProperty("mongoDB.user"),
                        System.getProperty("mongoDB.password"))
        );
        CloudSystem.setAPI(cloudSystemAPI);

        //Init EventHandler
        EventHandler eventHandler = new EventHandler(cloudSystemAPI.getNatsConnector().getConnection());
        CloudSystem.setIEventAPI(eventHandler);


    }


}
