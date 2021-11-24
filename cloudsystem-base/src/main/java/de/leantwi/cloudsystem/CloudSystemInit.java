package de.leantwi.cloudsystem;

import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.event.EventHandler;

public class CloudSystemInit {



    public CloudSystemInit() {

        EventHandler eventHandler = new EventHandler();
        CloudSystem.setIEventAPI(eventHandler);

        CloudSystemAPI cloudSystemAPI = new CloudSystemAPI();
        CloudSystem.setAPI(cloudSystemAPI);






    }


}
