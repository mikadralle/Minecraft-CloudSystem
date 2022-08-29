package de.leantwi.cloudsystem.wrapper.core;

import de.leantwi.cloudsystem.CloudSystem;
import de.leantwi.cloudsystem.api.CloudSystemAPI;
import de.leantwi.cloudsystem.api.gameserver.GameServerData;
import de.leantwi.cloudsystem.wrapper.WrapperBootstrap;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameServerHandler {

    private final CloudSystemAPI cloudSystemAPI = CloudSystem.getAPI();
    public final WrapperBootstrap wrapper = WrapperBootstrap.getInstance();

    private final Queue<String> gameServerQueue = new LinkedList<>();
    private final List<String> gameServerStartQueue = new ArrayList<>();

    //

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


        final GameServerData gameServerData = this.cloudSystemAPI.getGameServerByServerName(serverName);

        try {

            this.folderUtils.createTemp(gameServerData);
            Thread.sleep(500);

            final int memory = CloudSystem.getAPI().getSubGroupByName(gameServerData.getSubGroupDB()).get().getServerDB().getMemory();


            this.process = new ProcessBuilder(
                    "screen", "-AmdS", serverName.toLowerCase(),
                    "java", "-Xms" + memory + "M", "-Xmx" + memory + "M", "-jar", "spigot.jar")
                    .directory(new File(this.folderUtils.getPath(gameServerData) + "/" + gameServerData.getGroupDB() + "/" + gameServerData.getSubGroupDB() + "/" + serverName + "/")).inheritIO().start();

            WrapperBootstrap.getInstance().getLogger().info("§aServer §e" + serverName + "§a will be started.");


        } catch (IOException | InterruptedException e) {
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
