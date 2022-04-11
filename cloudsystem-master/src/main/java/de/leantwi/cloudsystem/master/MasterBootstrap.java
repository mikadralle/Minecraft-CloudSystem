package de.leantwi.cloudsystem.master;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.database.NatsConnectorAPI;
import de.leantwi.cloudsystem.api.events.global.ShutdownSystemEvent;
import de.leantwi.cloudsystem.master.api.config.IniFile;
import de.leantwi.cloudsystem.master.command.HelpCommand;
import de.leantwi.cloudsystem.master.command.StartCommand;
import de.leantwi.cloudsystem.master.command.StopCommand;
import de.leantwi.cloudsystem.master.database.mongo.DatabaseHandler;
import de.leantwi.cloudsystem.master.listeners.*;
import de.leantwi.cloudsystem.master.handler.bungeecord.BungeeHandler;
import de.leantwi.cloudsystem.master.handler.hoster.HosterCloud;
import de.leantwi.cloudsystem.master.handler.message.CloudDispatcher;
import de.leantwi.cloudsystem.master.handler.message.PingDispatcher;
import de.leantwi.cloudsystem.master.handler.message.VerifyDispatcher;
import de.leantwi.cloudsystem.master.handler.packets.handler.PacketHandler;
import de.leantwi.cloudsystem.master.handler.server.ServerFactory;
import de.leantwi.cloudsystem.master.handler.service.ServerOnlineAmountService;
import de.leantwi.cloudsystem.master.handler.service.TimerTaskService;
import de.leantwi.cloudsystem.master.handler.wrapper.WrapperHandler;
import de.leantwi.service.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import redis.clients.jedis.Jedis;

@Getter
public class MasterBootstrap extends Service {

    @Getter
    private static MasterBootstrap instance;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    //config
    private IniFile configAPI;
    //database & messaging
    private NatsConnectorAPI natsConnector;
    //dispatcher
    private CloudDispatcher cloudDispatcher;
    private VerifyDispatcher verifyDispatcher;
    private PingDispatcher pingDispatcher;
    //
    private DatabaseHandler databaseHandler;

    //handler's
    private WrapperHandler wrapperHandler;
    //factory
    private ServerFactory serverFactory;
    private BungeeHandler bungeeHandler;
    //
    // private Core core;
    private PacketHandler packetHandler;

    private CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    //
    //private HetznerCloudAPI hetznerCloudAPI;
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
        CloudSystem.getEventAPI().callEvent(new ShutdownSystemEvent("The cloudsystem will be shutdown."));
        this.getLogger().info("§cThe master will be stopped.");

    }

    public void sendMessage(String message) {
        getLogger().info(message);
    }

    private void initClass() {
        this.configAPI = new IniFile("config.yml");
        this.loadConfig();
        // connections
        this.natsConnector = this.cloudSystemAPI.getNatsConnector();
        // dispatcher
        this.cloudDispatcher = new CloudDispatcher();
        this.verifyDispatcher = new VerifyDispatcher();
        this.pingDispatcher = new PingDispatcher();
        //
        this.databaseHandler = new DatabaseHandler();
        this.packetHandler = new PacketHandler();
        this.wrapperHandler = new WrapperHandler();
        // this.core = new Core();
        this.serverFactory = new ServerFactory();
        this.bungeeHandler = new BungeeHandler();
        this.hosterCloud = new HosterCloud();
    }

    private void init() {
        //clearing the redis cache pool
        try (Jedis jedis = this.cloudSystemAPI.getRedisPool().getResource()) {
            jedis.select(7);
            jedis.del("cloud:server");
        }
        sleep(100);

        // dispatcher
        this.cloudDispatcher.listen(); // Wird überarbeitet.
        this.verifyDispatcher.listen(); // wird überarbeitet.
        this.pingDispatcher.listen(); // Wird überarbeitet.

        this.executorService.execute(new TimerTaskService());
        this.cloudSystemAPI.getNatsConnector().publish("info", "master_connected"); // Broadcast that the master is now online
        //TODO: Unnötog, aber leider noch wichtig.
        this.wrapperHandler.addPublicIP("wrapper-1", this.configAPI.getProperty("wrapper.master.address"));

        //this.hetznerCloudAPI = new HetznerCloudAPI(this.configAPI.getProperty("heztner.token"));

        CloudSystem.getEventAPI().registerListener(new MessageListener());
        CloudSystem.getEventAPI().registerListener(new PlayerMessageListener());
        CloudSystem.getEventAPI().registerListener(new PlayerChangeServerListener());
        CloudSystem.getEventAPI().registerListener(new StartGameServerListener());
        CloudSystem.getEventAPI().registerListener(new GameTypeChangeListener());


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

    public void startService() {
        this.executorService.execute(new ServerOnlineAmountService());
    }
}
