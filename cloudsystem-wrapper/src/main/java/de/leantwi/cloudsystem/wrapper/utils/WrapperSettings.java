package de.leantwi.cloudsystem.wrapper.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WrapperSettings {

  private String wrapperID;
  private int weightClass;
  private int priority;
  private WrapperType type;
  private boolean master;
  private String address;

}
