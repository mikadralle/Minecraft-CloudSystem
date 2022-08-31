package de.leantwi.cloudsystem.api.database.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MongoDBData {

    private String hostName, password, userName, authDB, defaultDB;
    private int port;
}
