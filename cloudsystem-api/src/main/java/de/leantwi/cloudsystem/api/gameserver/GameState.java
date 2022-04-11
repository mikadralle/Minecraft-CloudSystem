package de.leantwi.cloudsystem.api.gameserver;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GameState {

    STARTS("starts"),
    LOBBY("lobby"),
    GAME("game"),
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
            case "lobby":
                return LOBBY;
            case "game":
                return GAME;
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
