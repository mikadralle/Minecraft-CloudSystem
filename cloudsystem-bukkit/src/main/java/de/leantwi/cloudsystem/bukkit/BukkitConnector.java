package de.leantwi.cloudsystem.bukkit;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.CloudSystemInit;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.database.data.MongoDBData;
import de.leantwi.cloudsystem.api.database.data.NatsData;
import de.leantwi.cloudsystem.api.database.data.RedisData;
import de.leantwi.cloudsystem.bukkit.listeners.cloud.ShutdownSystemListener;
import de.leantwi.cloudsystem.bukkit.listeners.cloud.StopGameServerListener;
import de.leantwi.cloudsystem.bukkit.server.SpigotConnector;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class BukkitConnector extends JavaPlugin {

    @Getter
    private static BukkitConnector instance;

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    private ExecutorService executorService;
    private SpigotConnector spigotConnector;
    private CloudSystemInit cloudSystemInit;

    @Override
    public void onLoad() {


        this.cloudSystemInit = new CloudSystemInit(
                new RedisData(
                        System.getProperty("redis.hostname"),
                        System.getProperty("redis.password"),
                        Integer.parseInt(System.getProperty("redis.port")),
                        Integer.parseInt(System.getProperty("redis.databaseID"))),
                new MongoDBData(
                        System.getProperty("mongoDB.hostname"),
                        System.getProperty("mongoDB.password"),
                        System.getProperty("mongoDB.username"),
                        System.getProperty("mongoDB.authDB"),
                        System.getProperty("mongoDB.defaultDB"),
                        Integer.parseInt(System.getProperty("mongoDB.port"))),
                new NatsData(System.getProperty("nats.hostname"),
                        System.getProperty("nats.token"),
                        Integer.parseInt(System.getProperty("nats.port"))), null);

    }

    @Override
    public void onEnable() {


        instance = this;
        this.registerClasses();
        this.init();

    }

    @Override
    public void onDisable() {

        this.spigotConnector.logoutSpigotServer();

    }

    private void registerClasses() {
        this.executorService = Executors.newCachedThreadPool();
        this.spigotConnector = new SpigotConnector();
    }

    private void init() {

        //Cloud listeners
        CloudSystem.getEventAPI().registerListener(new ShutdownSystemListener());
        CloudSystem.getEventAPI().registerListener(new StopGameServerListener());

        this.spigotConnector.loginSpigotServer();
    }


}
