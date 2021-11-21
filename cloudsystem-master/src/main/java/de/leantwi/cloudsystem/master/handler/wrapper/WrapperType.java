package de.leantwi.cloudsystem.master.handler.wrapper;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum WrapperType {

  PUBLIC("public"),
  PRIVATE("private");

  private final String name;
}


