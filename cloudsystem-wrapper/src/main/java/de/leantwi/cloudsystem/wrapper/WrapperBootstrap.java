package de.leantwi.cloudsystem.wrapper;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.wrapper.commands.HelpCommand;
import de.leantwi.cloudsystem.wrapper.commands.StopCommand;
import de.leantwi.cloudsystem.wrapper.core.FolderUtils;
import de.leantwi.cloudsystem.wrapper.core.WrapperCore;
import de.leantwi.cloudsystem.wrapper.core.handler.TimerTaskHandler;
import de.leantwi.cloudsystem.wrapper.database.message.CloudDispatcher;
import de.leantwi.cloudsystem.wrapper.database.message.InformationDispatcher;
import de.leantwi.cloudsystem.wrapper.utils.config.IniFile;
import de.leantwi.service.Service;
import de.leantwi.cloudsystem.wrapper.utils.WrapperSettings;
import de.leantwi.cloudsystem.wrapper.utils.WrapperType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import lombok.Getter;
import lombok.Setter;

@Getter
public class WrapperBootstrap extends Service {

    @Getter
    private static WrapperBootstrap instance;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    //dispatcher
    private InformationDispatcher informationDispatcher;
    private CloudDispatcher cloudDispatcher;


    //
    private FolderUtils folderUtils;
    private WrapperCore wrapperCore;
    private IniFile configAPI;
    //
    private WrapperSettings wrapperSettings;
    @Setter
    private boolean isMasterOnline;

    public static void main(String[] strings) {

        instance = new WrapperBootstrap();
        instance.onEnable();

    }

    public void onEnable() {
        registerClasses();
        init();
    }


    @Override
    public void onBootstrap() {

    }

    @Override
    public void onShutdown() {


        this.cloudSystemAPI.getNatsConnector().publish("cloud", "stop_wrapper#" + this.wrapperSettings.getWrapperID());
        getLogger().info("§aStopping wrapper server...");
        getLogger().info("§aWrapper is stopping!");
    }

    @Override
    public void registerCommands() {

        getCommandHandler().registerCommand(new StopCommand());
        getCommandHandler().registerCommand(new HelpCommand());

    }

    public void sendMessage(String message) {
        getLogger().info(message);
    }

    private void init() {

        this.loadConfig();
        this.informationDispatcher.listen();
        String answer = null;
        try {
            answer = this.cloudSystemAPI.getNatsConnector().request("ping", "ping");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {

            System.out.println("Master is doesn't available!");
            return;
        }
        if (answer.equalsIgnoreCase("pong")) {
            this.loadWrapper();
            return;
        }
        System.out.println("Master is doesn't available!");


    }

    public void loadWrapper() {
        this.isMasterOnline = true;
        this.verifyWrapper();
        this.cloudDispatcher.listen();
        this.folderUtils.load();
        this.executorService.execute(new TimerTaskHandler());

    }

    private void registerClasses() {

        this.configAPI = new IniFile("config.ini");
        this.cloudDispatcher = new CloudDispatcher();
        this.folderUtils = new FolderUtils();
        this.wrapperCore = new WrapperCore();
        this.wrapperSettings = new WrapperSettings();
        this.informationDispatcher = new InformationDispatcher();

    }


    private String getHostname() {

        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void verifyWrapper() {


        getLogger().info("Load Config");
        this.loadConfig();
        getLogger().info("Config was loaded");
        this.wrapperSettings.setMaster(Boolean.parseBoolean(this.configAPI.getProperty("wrapper.master")));
        this.wrapperSettings.setPriority(this.wrapperSettings.isMaster() ? 100 : 50);
        this.wrapperSettings.setWeightClass(Integer.parseInt(this.configAPI.getProperty("wrapper.weight-class")));


        //TODO Update Wrapper id's
        if (this.wrapperSettings.isMaster()) {
            this.wrapperSettings.setWrapperID("wrapper-1");
        } else {
            this.wrapperSettings.setWrapperID(getHostname());
        }
        this.getLogger().info("The wrapper was assigned the " + this.wrapperSettings.getWrapperID());

        String type = this.configAPI.getProperty("wrapper.type");

        if (type.equalsIgnoreCase("public")) {
            this.wrapperSettings.setType(WrapperType.PUBLIC);
        } else {
            this.wrapperSettings.setType(WrapperType.PRIVATE);
        }
        getLogger().info("D");
        try {
            String answer = this.cloudSystemAPI.getNatsConnector().request("verify", "wrapper_register#" + wrapperSettings.getWrapperID() + "#" + this.wrapperSettings.getType().name() + "#" + this.wrapperSettings.getWeightClass() + "#" + this.wrapperSettings.getPriority());
            this.wrapperSettings.setAddress(answer);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            getLogger().warning(e.getMessage());
            e.printStackTrace();
        }
        getLogger().info("E");

    }

    private void loadConfig() {

        if (this.configAPI.isEmpty()) {
            // create nats configuration
            this.configAPI.setProperty("nats.hostname", "127.0.0.1");
            this.configAPI.setProperty("nats.port", "4222");
            this.configAPI.setProperty("nats.token", "token");
            // create redis configuration
            this.configAPI.setProperty("redis.hostname", "127.0.0.1");
            this.configAPI.setProperty("redis.port", "6379");
            this.configAPI.setProperty("redis.password", "passwd");
            this.configAPI.setProperty("redis.databaseID", "8");
            // create wrapper configuration
            this.configAPI.setProperty("wrapper.type", "public");
            this.configAPI.setProperty("wrapper.weight-class", "350");
            this.configAPI.setProperty("wrapper.master", "true");
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

