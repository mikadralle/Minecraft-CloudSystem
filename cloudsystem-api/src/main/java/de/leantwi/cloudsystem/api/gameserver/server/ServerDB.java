package de.leantwi.cloudsystem.api.gameserver.server;

import de.leantwi.cloudsystem.api.database.mongodb.DBDocument;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
@Getter
@Setter
public class ServerDB implements DBDocument {

    private String displayName, color;
    private int maxPlayer, memory, minOnlineAmount, maxOnlineAmount, startServerByPlayersLimit;
    private boolean maintenance, globalCheck;

    @Override
    public Document create() {
        return new Document().append("displayName", this.displayName).
                append("color", this.color).
                append("maxPlayer", this.maxPlayer).
                append("memory", this.memory).
                append("minOnlineAmount", this.minOnlineAmount).
                append("maxOnlineAmount", this.maxOnlineAmount).
                append("startServerByPlayersLimit", this.startServerByPlayersLimit).
                append("maintenance", this.maintenance).
                append("globalCheck", this.globalCheck);
    }

    @Override
    public void fetch(Document document) {

        if (document.containsKey("displayName")) {
            this.displayName = document.getString("displayName");
        }
        if (document.containsKey("color")) {
            this.color = document.getString("color");
        }
        if (document.containsKey("maxPlayer")) {
            this.maxPlayer = document.getInteger("maxPlayer");
        }
        if (document.containsKey("memory")) {
            this.memory = document.getInteger("memory");
        }
        if (document.containsKey("minOnlineAmount")) {
            this.minOnlineAmount = document.getInteger("minOnlineAmount");
        }
        if (document.containsKey("maxOnlineAmount")) {
            this.maxOnlineAmount = document.getInteger("maxOnlineAmount");
        }
        if (document.containsKey("startServerByPlayersLimit")) {
            this.startServerByPlayersLimit = document.getInteger("startServerByPlayersLimit");
        }
        if (document.containsKey("maintenance")) {
            this.maintenance = document.getBoolean("maintenance");
        }
        if (document.containsKey("globalCheck")) {
            this.globalCheck = document.getBoolean("globalCheck");
        }

    }
}
