package de.leantwi.cloudsystem.bukkit;

import de.leantwi.cloudsystem.bukkit.config.IniFile;
import de.leantwi.cloudsystem.bukkit.database.NatsConnector;
import de.leantwi.cloudsystem.bukkit.database.RedisConnector;
import de.leantwi.cloudsystem.bukkit.database.mongodb.MongoDBConnector;
import de.leantwi.cloudsystem.bukkit.messager.CloudDispatcher;
import de.leantwi.cloudsystem.bukkit.server.SpigotConnector;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class BukkitConnector extends JavaPlugin {


    @Getter
    private static BukkitConnector instance;
    private IniFile configAPI;
    private ExecutorService executorService;
    private NatsConnector natsConnector;
    private MongoDBConnector mongoDBConnector;
    private RedisConnector redisConnector;
    private SpigotConnector spigotConnector;
    private CloudDispatcher cloudDispatcher;

    @Override
    public void onEnable() {

        instance = this;
        this.registerClasses();
        this.init();

    }

    @Override
    public void onDisable() {
        this.spigotConnector.logoutSpigotServer();
        this.mongoDBConnector.disconnect();
        this.redisConnector.disconnect();

    }

    private void registerClasses() {
        this.executorService = Executors.newCachedThreadPool();
        this.configAPI = new IniFile("database.ini");
        this.redisConnector = new RedisConnector();
        this.mongoDBConnector = new MongoDBConnector();
        this.natsConnector = new NatsConnector();
        this.spigotConnector = new SpigotConnector();
        this.cloudDispatcher = new CloudDispatcher();
    }

    private void init() {
        this.loadConfig();

        this.redisConnector.connect();
        this.mongoDBConnector.connect();
        this.natsConnector.connect();

        this.spigotConnector.loginSpigotServer();
        this.cloudDispatcher.listen();

    }

    private void loadConfig() {
        if (configAPI.isEmpty()) {
            // Nats connection data's
            configAPI.setProperty("nats.host", "nats.leantwi.de");
            configAPI.setProperty("nats.port", "4222");
            configAPI.setProperty("nats.token", "token_key");
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
            configAPI.saveToFile();
        }
    }

}
