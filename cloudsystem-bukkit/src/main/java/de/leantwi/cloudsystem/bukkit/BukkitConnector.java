package de.leantwi.cloudsystem.bukkit;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.CloudSystemInit;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.bukkit.listeners.ShutdownSystemListener;
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

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    private ExecutorService executorService;
    private SpigotConnector spigotConnector;
    private CloudDispatcher cloudDispatcher;
    private CloudSystemInit cloudSystemInit;

    @Override
    public void onLoad() {
        this.cloudSystemInit = new CloudSystemInit();
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
        this.cloudDispatcher = new CloudDispatcher();
    }

    private void init() {

        //Cloud listeners
        CloudSystem.getEventAPI().registerListener(new ShutdownSystemListener());

        this.spigotConnector.loginSpigotServer();
        this.cloudDispatcher.listen();
    }


}
