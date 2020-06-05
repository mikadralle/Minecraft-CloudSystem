package eu.unyfy.master.handler.group;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GametState {

  LOBBY("lobby"),
  GAME("game"),
  RESTART("restart"),
  STATIC("static");

  private final String name;
}
