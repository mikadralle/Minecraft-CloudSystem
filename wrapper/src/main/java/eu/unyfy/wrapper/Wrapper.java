package eu.unyfy.wrapper;

import eu.unyfy.wrapper.api.config.IniFile;
import eu.unyfy.wrapper.core.FolderUtils;
import eu.unyfy.wrapper.core.WrapperCore;
import eu.unyfy.wrapper.core.handler.TimerTaskHandler;
import eu.unyfy.wrapper.database.message.MessageSystem;
import eu.unyfy.wrapper.database.nats.NatsConnector;
import eu.unyfy.wrapper.database.redis.RedisConnector;
import eu.unyfy.wrapper.utils.WrapperSettings;
import eu.unyfy.wrapper.utils.WrapperType;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
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
  private IniFile configAPI;
  //
  private WrapperSettings wrapperSettings;

  public static Wrapper getInstance() {
    return instance;
  }

  public void onEnable() {
    instance = this;
    registerClasses();
    this.loadConfig();
    init();
  }

  private void init() {
    this.redisConnector.connect();
    this.natsConnector.connect();
    this.verifyWrapper();
    this.messageSystem.listen();
    this.folderUtils.load();
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
    this.wrapperSettings = new WrapperSettings();

  }

  private String getHostname() {

    try {
      return InetAddress.getLocalHost().toString().split("/")[1];
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return null;
  }


  private void verifyWrapper() {
    this.loadConfig();
    this.wrapperSettings.setMaster(Boolean.parseBoolean(this.configAPI.getProperty("wrapper.master")));
    this.wrapperSettings.setPriority(this.wrapperSettings.isMaster() ? 100 : 50);
    this.wrapperSettings.setWeightClass(Integer.parseInt(this.configAPI.getProperty("wrapper.weight-class")));

    String type = this.configAPI.getProperty("wrapper.type");

    if (type.equalsIgnoreCase("public")) {
      this.wrapperSettings.setType(WrapperType.PUBLIC);
    } else {
      this.wrapperSettings.setType(WrapperType.PRIVATE);
    }

    String answer = null;
    try {
      answer = this.natsConnector.request("verify", "wrapper_register#" + getHostname() + "#" + this.wrapperSettings.getType().name() + "#" + this.wrapperSettings.getWeightClass() + "#" + this.wrapperSettings.getPriority());
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }
    this.wrapperSettings.setWrapperID(answer);
    System.out.println("Wrapper-ID: " + wrapperSettings.getWrapperID());
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
      // create wrapper configuration
      this.configAPI.setProperty("wrapper.type", "public");
      this.configAPI.setProperty("wrapper.weight-class", "350");
      this.configAPI.setProperty("wrapper.master", "true");
      this.configAPI.saveToFile();

    }

  }


}

