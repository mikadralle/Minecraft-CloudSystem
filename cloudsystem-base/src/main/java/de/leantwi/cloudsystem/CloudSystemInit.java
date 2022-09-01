package de.leantwi.cloudsystem;

import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.CloudSystemBase;
import de.leantwi.cloudsystem.api.database.data.MongoDBData;
import de.leantwi.cloudsystem.api.database.data.NatsData;
import de.leantwi.cloudsystem.api.database.data.RedisData;
import de.leantwi.cloudsystem.event.EventHandler;

public class CloudSystemInit {


    public CloudSystemInit(RedisData redisData, MongoDBData mongoDBData, NatsData natsData) {

        CloudSystemAPI cloudSystemAPI = new CloudSystemBase(natsData, redisData, mongoDBData);
        CloudSystem.setAPI(cloudSystemAPI);

        //Init EventHandler
        EventHandler eventHandler = new EventHandler(cloudSystemAPI.getNatsConnector().getConnection());
        CloudSystem.setIEventAPI(eventHandler);


    }


}
