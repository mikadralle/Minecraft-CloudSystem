package de.leantwi.cloudsystem.proxy;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.CloudSystemInit;
import de.leantwi.cloudsystem.api.database.data.MongoDBData;
import de.leantwi.cloudsystem.api.database.data.NatsData;
import de.leantwi.cloudsystem.api.database.data.RedisData;
import de.leantwi.cloudsystem.proxy.api.CloudProxy;
import de.leantwi.cloudsystem.proxy.command.CloudCommand;
import de.leantwi.cloudsystem.proxy.config.IniFile;
import de.leantwi.cloudsystem.proxy.listeners.*;
import de.leantwi.cloudsystem.proxy.listeners.players.CloudPlayerJoinNetworkListener;
import de.leantwi.cloudsystem.proxy.listeners.players.CloudPlayerQuitNetworkListener;
import de.leantwi.cloudsystem.proxy.listeners.players.ConnectCloudPlayerToServerListener;
import de.leantwi.cloudsystem.proxy.listeners.players.SendMessageToCloudPlayerListener;
import de.leantwi.cloudsystem.proxy.messager.BackendDispatcher;
import de.leantwi.cloudsystem.proxy.server.BungeeConnector;
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
    private CloudProxy cloudProxy;
    private BungeeConnector bungeeConnector;
    private CloudSystemInit cloudSystemInit;
    private BackendDispatcher backendDispatcher;

    public static ProxyConnector getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {

        IniFile configAPI = new IniFile("database.ini");
        this.loadConfig(configAPI);
        RedisData redisData = new RedisData(
                configAPI.getProperty("redis.hostname"),
                configAPI.getProperty("redis.password"),
                Integer.parseInt(configAPI.getProperty("redis.port")),
                Integer.parseInt(configAPI.getProperty("redis.databaseID")));

        MongoDBData mongoDBData = new MongoDBData(
                configAPI.getProperty("mongoDB.hostname"),
                configAPI.getProperty("mongoDB.password"),
                configAPI.getProperty("mongoDB.username"),
                configAPI.getProperty("mongoDB.authDB"),
                configAPI.getProperty("mongoDB.defaultDB"),
                Integer.parseInt(configAPI.getProperty("mongoDB.port")));

        NatsData natsData = new NatsData(
                configAPI.getProperty("nats.hostname"),
                configAPI.getProperty("nats.token"),
                Integer.parseInt(configAPI.getProperty("nats.port")));

        this.cloudSystemInit = new CloudSystemInit(redisData, mongoDBData, natsData);
    }

    @Override
    public void onEnable() {

        instance = this;
        this.registerClasses();
        this.init();

    }

    private void registerClasses() {

        this.backendDispatcher = new BackendDispatcher();
        this.bungeeConnector = new BungeeConnector();
        this.cloudProxy = new CloudProxy();

    }

    private void init() {
        this.backendDispatcher.listen();

        ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerConnectListener());
        CloudSystem.getEventAPI().registerListener(new StartGameServerListener());
        CloudSystem.getEventAPI().registerListener(new ShutdownSystemListener());
        CloudSystem.getEventAPI().registerListener(new GameTypeChangeListener());
        CloudSystem.getEventAPI().registerListener(new UnRegisterBungeeCordListener());
        CloudSystem.getEventAPI().registerListener(new ConnectCloudPlayerToServerListener());
        CloudSystem.getEventAPI().registerListener(new SendMessageToCloudPlayerListener());

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
        this.cloudProxy.logoutProxyServer();
    }

    private void loadConfig(IniFile config) {

        if (config.isEmpty()) {

            //redis connector configuration //
            config.setProperty("redis.hostname", "127.0.0.1");
            config.setProperty("redis.port", "6379");
            config.setProperty("redis.password", "password");
            config.setProperty("redis.databaseID", "7");

            //mongdb connector configuration //
            config.setProperty("mongoDB.hostname", "127.0.0.1");
            config.setProperty("mongoDB.username", "admin");
            config.setProperty("mongoDB.password", "password");
            config.setProperty("mongoDB.authDB", "admin");
            config.setProperty("mongoDB.defaultDB", "cloud");
            config.setProperty("mongoDB.port", "27017");

            // nats.io connector configuration
            config.setProperty("nats.hostname", "127.0.0.0.1");
            config.setProperty("nats.port", "4222");
            config.setProperty("nats.token", "token");

            config.saveToFile();
        }
    }

}
