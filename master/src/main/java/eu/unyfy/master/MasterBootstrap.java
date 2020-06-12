package eu.unyfy.master;

import eu.unyfy.master.api.config.IniFile;
import eu.unyfy.master.command.HelpCommand;
import eu.unyfy.master.command.StartCommand;
import eu.unyfy.master.command.StopCommand;
import eu.unyfy.master.database.mongo.DatabaseHandler;
import eu.unyfy.master.database.mongo.MongoDBConnector;
import eu.unyfy.master.database.nats.NatsConnector;
import eu.unyfy.master.database.redis.RedisConnector;
import eu.unyfy.master.handler.bungeecord.BungeeHandler;
import eu.unyfy.master.handler.core.Core;
import eu.unyfy.master.handler.group.GroupHandler;
import eu.unyfy.master.handler.hoster.HosterCloud;
import eu.unyfy.master.handler.message.CloudDispatcher;
import eu.unyfy.master.handler.message.PingDispatcher;
import eu.unyfy.master.handler.message.VerifyDispatcher;
import eu.unyfy.master.handler.packets.handler.PacketHandler;
import eu.unyfy.master.handler.server.ServerFactory;
import eu.unyfy.master.handler.service.TimerTaskService;
import eu.unyfy.master.handler.wrapper.WrapperHandler;
import eu.unyfy.service.Service;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
import me.tomsdevsn.hetznercloud.HetznerCloudAPI;

@Getter
public class MasterBootstrap extends Service {

  @Getter
  private static MasterBootstrap instance;

  private final ExecutorService executorService = Executors.newCachedThreadPool();
  //config
  private IniFile configAPI;
  //database & messaging
  private RedisConnector redisConnector;
  private MongoDBConnector mongoDBConnector;
  private NatsConnector natsConnector;
  //dispatcher
  private CloudDispatcher cloudDispatcher;
  private VerifyDispatcher verifyDispatcher;
  private PingDispatcher pingDispatcher;
  //
  private DatabaseHandler databaseHandler;

  //handler's
  private GroupHandler groupHandler;
  private WrapperHandler wrapperHandler;
  //factory
  private ServerFactory serverFactory;
  private BungeeHandler bungeeHandler;
  //
  private Core core;
  private PacketHandler packetHandler;
  //
  private HetznerCloudAPI hetznerCloudAPI;
  private HosterCloud hosterCloud;

  public static void main(String[] strings) {
    instance = new MasterBootstrap();
    instance.onEnable();
  }

  public void onEnable() {
    initClass();
    init();

  }

  @Override
  public void onBootstrap() {

  }

  @Override
  public void registerCommands() {
    getCommandHandler().registerCommand(new StopCommand());
    getCommandHandler().registerCommand(new HelpCommand());
    getCommandHandler().registerCommand(new StartCommand());
  }

  @Override
  public void onShutdown() {
    this.sleep(100);
    this.natsConnector.sendMessage("cloud", "stop#" + "master");
    this.redisConnector.disconnect();
    this.mongoDBConnector.disconnect();
    sleep(100);
    sendMessage("§acloud is stopping.");
  }

  public void sendMessage(String message) {
    getLogger().info(message);
  }

  private void initClass() {
    this.configAPI = new IniFile("config.yml");
    this.loadConfig();
    // connections
    this.natsConnector = new NatsConnector();
    this.redisConnector = new RedisConnector();
    this.mongoDBConnector = new MongoDBConnector();
    // dispatcher
    this.cloudDispatcher = new CloudDispatcher();
    this.verifyDispatcher = new VerifyDispatcher();
    this.pingDispatcher = new PingDispatcher();
    //
    this.databaseHandler = new DatabaseHandler();
    this.packetHandler = new PacketHandler();
    this.groupHandler = new GroupHandler();
    this.wrapperHandler = new WrapperHandler();
    this.core = new Core();
    this.serverFactory = new ServerFactory();
    this.bungeeHandler = new BungeeHandler();
    this.hosterCloud = new HosterCloud();
  }

  private void init() {
    this.natsConnector.connect();
    // dispatcher
    this.cloudDispatcher.listen();
    this.verifyDispatcher.listen();
    this.pingDispatcher.listen();
    //
    this.redisConnector.connect();
    this.mongoDBConnector.connect();
    this.groupHandler.fetch();
    this.executorService.execute(new TimerTaskService(this.core));
    this.natsConnector.sendMessage("info", "master_connected");
    this.wrapperHandler.addPublicIP("wrapper-1", this.configAPI.getProperty("wrapper.master.address"));

    this.hetznerCloudAPI = new HetznerCloudAPI(this.configAPI.getProperty("heztner.token"));

    // MasterBootstrap.getInstance().sendMessage("All Datacenter: " + this.hetznerCloudAPI.getDatacenters().getDatacenters().toString());

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

      this.configAPI.setProperty("heztner.token", "token");
      this.configAPI.setProperty("wrapper.master.address", "5.9.13.253");

      this.configAPI.saveToFile();
    }
  }

  private void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
