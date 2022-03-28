package de.leantwi.cloudsystem.api.gameserver;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GameState {

    STARTS("starts"),
    LOBBY("lobby"),
    GAME("game"),
    RESTART("restart"),
    STATIC("static"),
    SHUTDOWN("shutdown");

    private final String name;
}
