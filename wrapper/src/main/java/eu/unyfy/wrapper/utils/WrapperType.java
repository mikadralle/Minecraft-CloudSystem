package eu.unyfy.wrapper.utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WrapperType {

  PUBLIC("public"),
  PRIVATE("private");

  private final String name;
}
