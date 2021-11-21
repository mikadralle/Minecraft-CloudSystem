package de.leantwi.cloudsystem.bukkit.server;

import de.leantwi.cloudsystem.bukkit.BukkitConnector;
import de.leantwi.cloudsystem.bukkit.collection.SessionServer;
import de.leantwi.cloudsystem.bukkit.database.NatsConnector;
import de.leantwi.cloudsystem.bukkit.server.state.GameType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
@RequiredArgsConstructor

public class ServerFactory {


    private final NatsConnector natsConnector = BukkitConnector.getInstance().getNatsConnector();

    private final String serverName;

    private GameType gameType = GameType.LOBBY;
    private SessionServer sessionServer;
    private SpigotConnector serverDB;

    public void fetch() {
        this.sessionServer = new SessionServer();
        this.sessionServer.fetch(this.serverName);
        Bukkit.getConsoleSender().sendMessage("Slots: " + this.sessionServer.getSlots());

        //todo: serverDB load
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
        this.sessionServer.update("gameState", gameType.toString().toUpperCase());
        this.natsConnector.publishMessage("cloud", "updateGameState#" + this.serverName + "#" + gameType.toString());
    }

    public void updateGameType(String gameType) {
        this.gameType = GameType.valueOf(gameType);
    }

}
