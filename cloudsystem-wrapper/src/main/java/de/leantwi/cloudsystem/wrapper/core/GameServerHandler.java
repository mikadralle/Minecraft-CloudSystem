package de.leantwi.cloudsystem.wrapper.core;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.wrapper.SetupServerHandlerAPI;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;
import de.leantwi.cloudsystem.wrapper.core.folder.SpigotFolderHandler;
import lombok.Getter;

import java.util.*;

public class GameServerHandler {

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    public final WrapperBootstrap wrapper = WrapperBootstrap.getInstance();

    private final Queue<String> gameServerQueue = new LinkedList<>();
    private final List<String> gameServerStartQueue = new ArrayList<>();
    @Getter
    private final Map<String, Process> processMap = new HashMap<>();
    private SetupServerHandlerAPI currentServerSetup;

    private final FolderUtils folderUtils = this.wrapper.getFolderUtils();
    @Getter
    private Process process;

    public void startServer() {
        if (this.gameServerQueue.isEmpty()) {
            return;
        }

        if (this.gameServerStartQueue.isEmpty()) {
            final String serverName = this.gameServerQueue.peek();
            this.gameServerStartQueue.add(serverName);
            this.start(serverName);
        }
    }


    private void start(String serverName) {

        try {


            this.currentServerSetup = new SpigotFolderHandler(this.cloudSystemAPI.getGameServerByServerName(serverName));
            this.currentServerSetup.setup(); // create folders and copy the folder from temp folder
            Thread.sleep(500);
            this.currentServerSetup.startServer(); // start server

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addRequestedGameServer(String serverName) {
        this.gameServerQueue.add(serverName);
    }

    public void finishServer(String serverName) {
        this.gameServerStartQueue.remove(serverName);
        this.gameServerQueue.remove(serverName);
    }


}
