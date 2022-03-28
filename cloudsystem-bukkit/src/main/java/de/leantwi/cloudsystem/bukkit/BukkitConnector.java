package de.leantwi.cloudsystem.bukkit;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.bukkit.config.IniFile;
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

    private CloudSystemAPI cloudSystemAPI;
    private ExecutorService executorService;
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

    }

    private void registerClasses() {
        this.executorService = Executors.newCachedThreadPool();
        this.cloudSystemAPI = CloudSystem.getAPI();
        this.spigotConnector = new SpigotConnector();
        this.cloudDispatcher = new CloudDispatcher();
    }

    private void init() {
        this.spigotConnector.loginSpigotServer();
        this.cloudDispatcher.listen();
    }


}
