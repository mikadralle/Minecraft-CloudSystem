package de.leantwi.cloudsystem.wrapper;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.events.wrapper.UnRegisterWrapperEvent;
import de.leantwi.cloudsystem.wrapper.commands.HelpCommand;
import de.leantwi.cloudsystem.wrapper.commands.StopCommand;
import de.leantwi.cloudsystem.wrapper.core.FolderUtils;
import de.leantwi.cloudsystem.wrapper.core.GameServerHandler;
import de.leantwi.cloudsystem.wrapper.core.handler.TimerTaskHandler;
import de.leantwi.cloudsystem.wrapper.database.message.CloudDispatcher;
import de.leantwi.cloudsystem.wrapper.database.message.InformationDispatcher;
import de.leantwi.cloudsystem.wrapper.listeners.RequestGameServerListener;
import de.leantwi.cloudsystem.wrapper.listeners.ShutdownSystemListener;
import de.leantwi.cloudsystem.wrapper.listeners.UpdateGameServerStatusListener;
import de.leantwi.cloudsystem.wrapper.listeners.groups.RefreshGroupsListener;
import de.leantwi.cloudsystem.wrapper.utils.WrapperSettings;
import de.leantwi.cloudsystem.wrapper.utils.WrapperType;
import de.leantwi.service.Service;
import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

@Getter
public class WrapperBootstrap extends Service {

    @Getter
    private static WrapperBootstrap instance;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    //dispatcher
    private InformationDispatcher informationDispatcher;
    private CloudDispatcher cloudDispatcher;

    private FolderUtils folderUtils;
    private GameServerHandler gameServerHandler;
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
        CloudSystem.getEventAPI().callEvent(new UnRegisterWrapperEvent(wrapperSettings.getWrapperID()));
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

        //Listeners

        CloudSystem.getEventAPI().registerListener(new ShutdownSystemListener());
        CloudSystem.getEventAPI().registerListener(new RequestGameServerListener());
        CloudSystem.getEventAPI().registerListener(new RefreshGroupsListener());
        CloudSystem.getEventAPI().registerListener(new UpdateGameServerStatusListener());

        this.informationDispatcher.listen();
        String answer = null;
        try {
            answer = this.cloudSystemAPI.getNatsConnector().request("ping", "ping");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            getLogger().info("Master is doesn't available:" + e.getMessage());
            return;
        }
        if (answer.equalsIgnoreCase("pong")) {
            this.loadWrapper();
            return;
        }
        getLogger().info("Master is doesn't available:");

    }

    public void loadWrapper() {
        this.isMasterOnline = true;
        this.verifyWrapper();
        this.cloudDispatcher.listen();
        this.executorService.execute(new TimerTaskHandler());
        getLogger().info("The wrapper is ready to start!");


    }

    private void registerClasses() {

        this.cloudDispatcher = new CloudDispatcher();
        this.folderUtils = new FolderUtils();
        this.gameServerHandler = new GameServerHandler();
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


        //getLogger().info("wrapper config will be loaded...");
        // this.loadConfig();
        getLogger().info("wrapper config has been load.");
        this.wrapperSettings.setMaster(true);
        this.wrapperSettings.setPriority(this.wrapperSettings.isMaster() ? 100 : 50);
        this.wrapperSettings.setWeightClass(9999);


        //TODO Update Wrapper id's
        if (this.wrapperSettings.isMaster()) {
            this.wrapperSettings.setWrapperID("wrapper-1");
        } else {
            this.wrapperSettings.setWrapperID(getHostname());
        }
        this.getLogger().info("The wrapper was assigned the " + this.wrapperSettings.getWrapperID());

        String type = "public";// this.configAPI.getProperty("wrapper.type");

        if (type.equalsIgnoreCase("public")) {
            this.wrapperSettings.setType(WrapperType.PUBLIC);
        } else {
            this.wrapperSettings.setType(WrapperType.PRIVATE);
        }
        try {
            String answer = this.cloudSystemAPI.getNatsConnector().request("verify", "wrapper_register#" + wrapperSettings.getWrapperID() + "#" + this.wrapperSettings.getType().name() + "#" + this.wrapperSettings.getWeightClass() + "#" + this.wrapperSettings.getPriority());
            this.wrapperSettings.setAddress(answer);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            getLogger().warning(e.getMessage());
            e.printStackTrace();
        }

    }


}

