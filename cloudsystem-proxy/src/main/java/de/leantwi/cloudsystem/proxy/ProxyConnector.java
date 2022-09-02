package de.leantwi.cloudsystem.proxy;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.CloudSystemInit;
import de.leantwi.cloudsystem.api.database.data.MongoDBData;
import de.leantwi.cloudsystem.api.database.data.NatsData;
import de.leantwi.cloudsystem.api.database.data.RedisData;
import de.leantwi.cloudsystem.proxy.command.CloudCommand;
import de.leantwi.cloudsystem.proxy.config.IniFile;
import de.leantwi.cloudsystem.proxy.listeners.*;
import de.leantwi.cloudsystem.proxy.listeners.players.CloudPlayerJoinNetworkListener;
import de.leantwi.cloudsystem.proxy.listeners.players.CloudPlayerQuitNetworkListener;
import de.leantwi.cloudsystem.proxy.listeners.players.PlayerSendMessageListener;
import de.leantwi.cloudsystem.proxy.messager.BackendDispatcher;
import de.leantwi.cloudsystem.proxy.server.BungeeConnector;
import de.leantwi.cloudsystem.proxy.server.ProxyHandler;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class ProxyConnector extends Plugin {

    private static ProxyConnector instance;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final String cloudPrefix = "§bCloud §8┃§7 ";
    private IniFile configAPI;
    private ProxyHandler proxyHandler;
    private BungeeConnector bungeeConnector;
    private CloudSystemInit cloudSystemInit;
    private BackendDispatcher backendDispatcher;

    public static ProxyConnector getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {

        this.configAPI = new IniFile("database.ini");
        RedisData redisData = new RedisData(
                this.configAPI.getProperty("redis.hostname"),
                this.configAPI.getProperty("redis.password"),
                Integer.parseInt(this.configAPI.getProperty("redis.port")),
                Integer.parseInt(this.configAPI.getProperty("redis.databaseID")));

        MongoDBData mongoDBData = new MongoDBData(
                this.configAPI.getProperty("mongoDB.hostname"),
                this.configAPI.getProperty("mongoDB.password"),
                this.configAPI.getProperty("mongoDB.username"),
                this.configAPI.getProperty("mongoDB.authDB"),
                this.configAPI.getProperty("mongoDB.defaultDB"),
                Integer.parseInt(this.configAPI.getProperty("mongoDB.port")));

        NatsData natsData = new NatsData(
                this.configAPI.getProperty("nats.hostname"),
                this.configAPI.getProperty("nats.token"),
                Integer.parseInt(this.configAPI.getProperty("nats.port")));

        this.cloudSystemInit = new CloudSystemInit(redisData, mongoDBData, natsData);
    }

    @Override
    public void onEnable() {

        instance = this;
        this.registerClasses();
        this.init();

    }

    private void registerClasses() {

        this.configAPI = new IniFile("database.ini");

        this.backendDispatcher = new BackendDispatcher();
        this.proxyHandler = new ProxyHandler();
        this.bungeeConnector = new BungeeConnector();


    }

    private void init() {
        this.backendDispatcher.listen();
        this.proxyHandler.loginProxyServer();

        ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerConnectListener());
        CloudSystem.getEventAPI().registerListener(new StartGameServerListener());
        CloudSystem.getEventAPI().registerListener(new ShutdownSystemListener());
        CloudSystem.getEventAPI().registerListener(new GameTypeChangeListener());
        CloudSystem.getEventAPI().registerListener(new UnRegisterBungeeCordListener());
        CloudSystem.getEventAPI().registerListener(new PlayerSendMessageListener());

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CloudCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new LoginListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new LogoutListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerConnectedListener());

        CloudSystem.getEventAPI().registerListener(new CloudPlayerJoinNetworkListener());
        CloudSystem.getEventAPI().registerListener(new CloudPlayerQuitNetworkListener());
        //ProxyServer.getInstance().getPluginManager().registerListener(this, new KickListener());

    }


    @Override
    public void onDisable() {
        this.proxyHandler.logoutProxyServer();
    }

    private void loadConfig() {

        if (this.configAPI.isEmpty()) {

            //redis connector configuration //
            this.configAPI.setProperty("redis.hostname", "127.0.0.1");
            this.configAPI.setProperty("redis.port", "6379");
            this.configAPI.setProperty("redis.password", "password");
            this.configAPI.setProperty("redis.databaseID", "7");

            //mongdb connector configuration //
            this.configAPI.setProperty("mongoDB.hostname", "127.0.0.1");
            this.configAPI.setProperty("mongoDB.username", "admin");
            this.configAPI.setProperty("mongoDB.password", "password");
            this.configAPI.setProperty("mongoDB.authDB", "admin");
            this.configAPI.setProperty("mongoDB.defaultDB", "cloud");
            this.configAPI.setProperty("mongoDB.port", "27017");

            // nats.io connector configuration
            this.configAPI.setProperty("nats.hostname", "127.0.0.0.1");
            this.configAPI.setProperty("nats.port", "4222");
            this.configAPI.setProperty("nats.token", "token");

            this.configAPI.saveToFile();
        }
    }

}
