package de.leantwi.cloudsystem.api.database.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedisData {

    private String hostName, password;
    private int port, databaseID;

}
