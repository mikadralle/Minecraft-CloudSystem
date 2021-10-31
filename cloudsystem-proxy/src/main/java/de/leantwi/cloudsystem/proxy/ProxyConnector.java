package de.leantwi.cloudsystem.proxy;

import de.leantwi.cloudsystem.proxy.config.IniFile;
import de.leantwi.cloudsystem.proxy.database.NatsConnector;
import de.leantwi.cloudsystem.proxy.database.RedisConnector;
import de.leantwi.cloudsystem.proxy.database.mongodb.MongoDBConnector;
import de.leantwi.cloudsystem.proxy.listener.KickListener;
import de.leantwi.cloudsystem.proxy.listener.ServerConnectHandler;
import de.leantwi.cloudsystem.proxy.listener.ServerConnectListener;
import de.leantwi.cloudsystem.proxy.messager.BackendDispatcher;
import de.leantwi.cloudsystem.proxy.messager.CloudDispatcher;
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
    private final String cloudPrefix = "§cCloud §8┃§7 ";
    private IniFile configAPI;
    private MongoDBConnector mongoDBConnector;
    private RedisConnector redisConnector;
    private NatsConnector natsConnector;
    private ProxyHandler proxyHandler;
    private BungeeConnector bungeeConnector;

    private CloudDispatcher cloudDispatcher;
    private BackendDispatcher backendDispatcher;

    public static ProxyConnector getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;
        this.registerClasses();
        this.init();

    }

    private void registerClasses() {

        this.configAPI = new IniFile("database.ini");
        this.mongoDBConnector = new MongoDBConnector();
        this.redisConnector = new RedisConnector();
        this.natsConnector = new NatsConnector();

        this.cloudDispatcher = new CloudDispatcher();
        this.backendDispatcher = new BackendDispatcher();
        this.proxyHandler = new ProxyHandler();
        this.bungeeConnector = new BungeeConnector();
    }

    private void init() {
        this.loadConfig();
        this.mongoDBConnector.connect();
        this.redisConnector.connect();
        this.natsConnector.connect();
        this.backendDispatcher.listen();
        this.cloudDispatcher.listen();
        this.proxyHandler.loginProxyServer();

        ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerConnectHandler());
        //ProxyServer.getInstance().getPluginManager().registerListener(this, new KickListener());

    }


    @Override
    public void onDisable() {
        this.proxyHandler.logoutProxyServer();
    }

    private void loadConfig() {

        if (configAPI.isEmpty()) {
            // MongoDB connection data's
            configAPI.setProperty("mongoDB.host", "127.0.0.1");
            configAPI.setProperty("mongoDB.authDB", "admin");
            configAPI.setProperty("mongoDB.user", "unyfy");
            configAPI.setProperty("mongoDB.password", "JBfd");
            // Redis connection data's
            configAPI.setProperty("redis.host", "127.0.0.1");
            configAPI.setProperty("redis.password", "BlinkerNachLinks2");
            configAPI.setProperty("redis.port", "6379");
            configAPI.setProperty("redis.databaseID", "10");
            // Nats connection data's
            configAPI.addDefault("nats.host", "nats.leantwi.de");
            configAPI.addDefault("nats.port", "4222");
            configAPI.addDefault("nats.token", "token_key");

            configAPI.saveToFile();
        }
    }

}
