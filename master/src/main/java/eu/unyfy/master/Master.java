package eu.unyfy.master;

import eu.unyfy.master.api.config.ConfigAPI;
import eu.unyfy.master.api.console.Console;
import eu.unyfy.master.command.handler.CommandHandler;
import eu.unyfy.master.database.MainDatabase;
import eu.unyfy.master.database.mongo.DatabaseHandler;
import eu.unyfy.master.database.mongo.MongoDBConnector;
import eu.unyfy.master.database.nats.NatsConnector;
import eu.unyfy.master.database.redis.RedisConnector;
import eu.unyfy.master.handler.bungeecord.BungeeHandler;
import eu.unyfy.master.handler.core.Core;
import eu.unyfy.master.handler.group.GroupHandler;
import eu.unyfy.master.handler.message.CloudDispatcher;
import eu.unyfy.master.handler.message.VerifyDispatcher;
import eu.unyfy.master.handler.packets.handler.PacketHandler;
import eu.unyfy.master.handler.server.ServerFactory;
import eu.unyfy.master.handler.service.TimerTaskService;
import eu.unyfy.master.handler.wrapper.WrapperHandler;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Getter
public class Master {

  private static Master instance;
  private final ExecutorService executorService = Executors.newCachedThreadPool();
  //config
  private ConfigAPI configAPI;
  //database & messaging
  private RedisConnector redisConnector;
  private MongoDBConnector mongoDBConnector;
  private NatsConnector natsConnector;
  //dispatcher
  private CloudDispatcher cloudDispatcher;
  private VerifyDispatcher verifyDispatcher;
  //
  private DatabaseHandler databaseHandler;
  private MainDatabase mainDatabase;

  //handler's
  private GroupHandler groupHandler;
  private WrapperHandler wrapperHandler;
  //factory
  private ServerFactory serverFactory;
  private BungeeHandler bungeeHandler;
  //
  private Core core;
  private PacketHandler packetHandler;

  private CommandHandler commandHandler;
  private Console console;


  public static Master getInstance() {
    return instance;
  }

  public void load() {

    registerClasses();
    init();

  }

  private void registerClasses() {
    instance = this;
    this.configAPI = new ConfigAPI();
    // connections
    this.natsConnector = new NatsConnector();
    this.redisConnector = new RedisConnector();
    this.mongoDBConnector = new MongoDBConnector();
    // dispatcher
    this.cloudDispatcher = new CloudDispatcher();
    this.verifyDispatcher = new VerifyDispatcher();
    //
    this.databaseHandler = new DatabaseHandler();
    this.mainDatabase = new MainDatabase();
    this.packetHandler = new PacketHandler();
    this.groupHandler = new GroupHandler();
    this.wrapperHandler = new WrapperHandler();
    this.core = new Core();
    this.serverFactory = new ServerFactory();
    this.bungeeHandler = new BungeeHandler();
    commandHandler = new CommandHandler();
    console = new Console();

  }

  private void init() {
    this.natsConnector.connect();
    // dispatcher
    this.cloudDispatcher.listen();
    this.verifyDispatcher.listen();
    //
    this.redisConnector.connect();
    this.mongoDBConnector.connect();
    this.clearCache();
    this.groupHandler.fetch();
    this.executorService.execute(new TimerTaskService(this.core));
    this.commandHandler.registerCommands();

  }

  private void clearCache() {
    try (Jedis jedis = this.redisConnector.getJedisPool().getResource()) {
      final Set<String> list = jedis.keys("cloud:*");
      final Pipeline pipeline = jedis.pipelined();
      list.forEach(pipeline::del);
      pipeline.sync();
    }
  }
}
