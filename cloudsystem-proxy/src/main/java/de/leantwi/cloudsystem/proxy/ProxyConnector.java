package de.leantwi.cloudsystem.proxy;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.CloudSystemInit;
import de.leantwi.cloudsystem.proxy.config.IniFile;
import de.leantwi.cloudsystem.proxy.listeners.ShutdownSystemListener;
import de.leantwi.cloudsystem.proxy.listeners.StartGameServerListener;
import de.leantwi.cloudsystem.proxy.listeners.ServerConnectListener;
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
    private final String cloudPrefix = "§bCloud §8┃§7 ";
    private IniFile configAPI;
    private ProxyHandler proxyHandler;
    private BungeeConnector bungeeConnector;
    private CloudSystemInit cloudSystemInit;
    private CloudDispatcher cloudDispatcher;
    private BackendDispatcher backendDispatcher;

    public static ProxyConnector getInstance() {
        return instance;
    }

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

    private void registerClasses() {

        this.configAPI = new IniFile("database.ini");

        this.cloudDispatcher = new CloudDispatcher();
        this.backendDispatcher = new BackendDispatcher();
        this.proxyHandler = new ProxyHandler();
        this.bungeeConnector = new BungeeConnector();
    }

    private void init() {
        this.backendDispatcher.listen();
        this.cloudDispatcher.listen();
        this.proxyHandler.loginProxyServer();

        ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerConnectListener());
        CloudSystem.getEventAPI().registerListener(new StartGameServerListener());
        CloudSystem.getEventAPI().registerListener(new ShutdownSystemListener());
        //ProxyServer.getInstance().getPluginManager().registerListener(this, new KickListener());

    }


    @Override
    public void onDisable() {
        this.proxyHandler.logoutProxyServer();
    }


}
