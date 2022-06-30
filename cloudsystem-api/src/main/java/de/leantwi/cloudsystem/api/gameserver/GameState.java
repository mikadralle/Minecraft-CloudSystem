package de.leantwi.cloudsystem.api.gameserver;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GameState {

    /*

    0 = OFFLINE
    1 = STARTED
    20 = LOBBY
    30 = InGame
    40 = RESTART

     */

    STARTS("starts"),
    STARTED("started"),
    LOBBY("lobby"),
    InGAME("ingame"),
    RESTART("restart"),
    STATIC("static"),
    SHUTDOWN("shutdown"),
    UNKNOWN("unknown");
    private final String name;

    public String getName() {
        return name;
    }

    public static GameState getGameStateByString(String type) {
        switch (type) {
            case "starts":
                return STARTS;
            case "started":
                return STARTED;
            case "lobby":
                return LOBBY;
            case "ingame":
                return InGAME;
            case "restart":
                return RESTART;
            case "static":
                return STATIC;
            case "shutdown":
                return SHUTDOWN;
            default:
                return UNKNOWN;
        }
    }
}
