
package de.leantwi.cloudsystem.api.wrapper;

import de.leantwi.cloudsystem.api.gameserver.GameServerData;

public interface SetupServerHandlerAPI {

    void setup();

    void startServer();


    void setSpigotYML(GameServerData gameServerData);

    void setBukkitYML(GameServerData gameServerData);

    void setServerProperties(GameServerData gameServerData);


    void setCloudConfig(GameServerData gameServerData);


}
