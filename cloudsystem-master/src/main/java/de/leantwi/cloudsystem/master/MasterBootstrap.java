package de.leantwi.cloudsystem.master;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.master.api.config.IniFile;
import de.leantwi.cloudsystem.master.command.HelpCommand;
import de.leantwi.cloudsystem.master.command.StartCommand;
import de.leantwi.cloudsystem.master.command.StopCommand;
import de.leantwi.cloudsystem.master.database.mongo.DatabaseHandler;
import de.leantwi.cloudsystem.master.database.mongo.MongoDBConnector;
import de.leantwi.cloudsystem.master.database.nats.NatsConnector;
import de.leantwi.cloudsystem.master.database.redis.RedisConnector;
import de.leantwi.cloudsystem.master.events.MessageListener;
import de.leantwi.cloudsystem.master.events.PlayerChangeServerListener;
import de.leantwi.cloudsystem.master.events.PlayerMessageListener;
import de.leantwi.cloudsystem.master.handler.bungeecord.BungeeHandler;
import de.leantwi.cloudsystem.master.handler.core.Core;
import de.leantwi.cloudsystem.master.handler.group.GroupHandler;
import de.leantwi.cloudsystem.master.handler.hoster.HosterCloud;
import de.leantwi.cloudsystem.master.handler.message.CloudDispatcher;
import de.leantwi.cloudsystem.master.handler.message.PingDispatcher;
import de.leantwi.cloudsystem.master.handler.message.VerifyDispatcher;
import de.leantwi.cloudsystem.master.handler.packets.handler.PacketHandler;
import de.leantwi.cloudsystem.master.handler.server.ServerFactory;
import de.leantwi.cloudsystem.master.handler.service.TimerTaskService;
import de.leantwi.cloudsystem.master.handler.wrapper.WrapperHandler;
import de.leantwi.service.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;

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
    //private HetznerCloudAPI hetznerCloudAPI;
    private HosterCloud hosterCloud;

    public static void main(String[] strings) {
        instance = new MasterBootstrap();
    instance.onEnable();
    }
    public void onEnable(){
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
/*
    this.getHetznerCloudAPI().getServers().getServers().forEach(server -> {
      this.getLogger().info("Hetzner-cloud server " + server.getName() + " will be deleted.");
      this.hetznerCloudAPI.deleteServer(server.getId());
    });


 */
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

        //this.hetznerCloudAPI = new HetznerCloudAPI(this.configAPI.getProperty("heztner.token"));

        CloudSystem.getEventAPI().registerListener(new MessageListener());
        CloudSystem.getEventAPI().registerListener(new PlayerMessageListener());
        CloudSystem.getEventAPI().registerListener(new PlayerChangeServerListener());


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
