package de.leantwi.cloudsystem;

import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.database.INats;
import de.leantwi.cloudsystem.database.NatsConnector;
import de.leantwi.cloudsystem.event.EventHandler;

import java.util.Properties;

public class CloudSystemInit {


    public CloudSystemInit() {

        //SET PROPERTIES
        Properties properties = System.getProperties();
        properties.setProperty("nats.hostname", "nats.leantwi.de");
        properties.setProperty("nats.token", "C1adWuKDBMR8WXwgTTyLeyKebmVJcmwY");
        properties.setProperty("nats.port", "4222");

        //Init EventHandler
        EventHandler eventHandler = new EventHandler();
        CloudSystem.setIEventAPI(eventHandler);

        System.out.println("Host:" + System.getProperty("nats.hostname"));
        System.out.println("token:" + System.getProperty("nats.token"));
        System.out.println("port:" + System.getProperty("nats.port"));
        System.out.println("name:" + System.getProperty("name"));

        CloudSystemAPI cloudSystemAPI = new CloudSystemAPI(new NatsConnector(
                System.getProperty("nats.hostname"),
                System.getProperty("nats.token"), 4222)
        );
        CloudSystem.setAPI(cloudSystemAPI);


    }


}
