package eu.unyfy.master.api.config;

import eu.unyfy.master.Master;
import lombok.Getter;

@Getter
public class ConfigAPI {

  private final Master master = Master.getInstance();
  private final IniFile configAPI = new IniFile("config.yml");
  private boolean multiWrapper;

  public ConfigAPI() {
    loadConfig();
  }


  private void loadConfig() {

    if (this.configAPI.isEmpty()) {

      //redis connector configuration //
      this.configAPI.setProperty("redis.host", "127.0.0.1");
      this.configAPI.setProperty("redis.port", "6379");
      this.configAPI.setProperty("redis.password", "password");
      this.configAPI.setProperty("redis.databaseID", "7");

      //mongdb connector configuration //
      this.configAPI.setProperty("mongoDB.host", "127.0.0.1");
      this.configAPI.setProperty("mongoDB.user", "admin");
      this.configAPI.setProperty("mongoDB.password", "password");
      this.configAPI.setProperty("mongoDB.authDB", "admin");
      this.configAPI.setProperty("mongoDB.defaultDB", "cloud");

      // nats.io connector configuration
      this.configAPI.setProperty("nats.hostname", "127.0.0.0.1");
      this.configAPI.setProperty("nats.port", "4222");
      this.configAPI.setProperty("nats.token", "token");

      this.configAPI.saveToFile();
    }

  }

}
