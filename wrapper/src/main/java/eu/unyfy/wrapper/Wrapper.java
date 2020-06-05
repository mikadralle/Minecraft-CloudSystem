package eu.unyfy.wrapper;

import eu.unyfy.wrapper.api.config.IniFile;
import eu.unyfy.wrapper.core.FolderUtils;
import eu.unyfy.wrapper.core.WrapperCore;
import eu.unyfy.wrapper.core.handler.TimerTaskHandler;
import eu.unyfy.wrapper.database.message.MessageSystem;
import eu.unyfy.wrapper.database.nats.NatsConnector;
import eu.unyfy.wrapper.database.redis.RedisConnector;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;

@Getter
public class Wrapper {

  private static Wrapper instance;
  private final ExecutorService executorService = Executors.newCachedThreadPool();
  //database and messaging
  private RedisConnector redisConnector;
  private NatsConnector natsConnector;
  private MessageSystem messageSystem;
  //
  private FolderUtils folderUtils;
  private WrapperCore wrapperCore;
  private String wrapperID;
  private IniFile configAPI;

  public static Wrapper getInstance() {
    return instance;
  }

  public void onEnable() {
    instance = this;
    registerClasses();
    this.loadConfig();
    this.wrapperID = this.configAPI.getProperty("wrapper.id");
    init();

  }

  private void init() {
    this.redisConnector.connect();
    this.natsConnector.connect();
    this.messageSystem.listen();
    this.folderUtils.load();
    this.natsConnector.sendMessage("cloud", "wrapper_login#" + this.wrapperID + "#" + this.configAPI.getProperty("wrapper.memory") + "#" + this.getHostname());
    this.executorService.execute(new TimerTaskHandler());
  }

  public void onDisable() {

    this.redisConnector.disconnect();

  }

  private void registerClasses() {

    this.configAPI = new IniFile("config.ini");
    this.natsConnector = new NatsConnector();
    this.messageSystem = new MessageSystem();
    this.redisConnector = new RedisConnector();
    this.folderUtils = new FolderUtils();
    this.wrapperCore = new WrapperCore();

  }

  private String getHostname() {

    try {
      return InetAddress.getLocalHost().toString().split("/")[1];
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void loadConfig() {

    if (this.configAPI.isEmpty()) {
      // create nats configuration
      this.configAPI.setProperty("nats.hostname", "127.0.0.1");
      this.configAPI.setProperty("nats.port", "4222");
      this.configAPI.setProperty("nats.token", "token");
      // create redis configuration
      this.configAPI.setProperty("redis.hostname", "127.0.0.1");
      this.configAPI.setProperty("redis.port", "6379");
      this.configAPI.setProperty("redis.password", "passwd");
      this.configAPI.setProperty("redis.databaseID", "8");

      this.configAPI.setProperty("wrapper.id", "wrapper-01");
      this.configAPI.setProperty("wrapper.memory", "65536");

      this.configAPI.saveToFile();

    }

  }

}

