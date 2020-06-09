package eu.unyfy.master.handler.hoster;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HetnerType {

  CX11("CX11", 1, 2, 20),
  CPX11("CPX11", 2, 2, 40),
  CX21("CX21", 2, 4, 40),
  CPX21("CPX21", 3, 4, 80),
  CX31("CX31", 2, 8, 80),
  CPX31("CPX31", 4, 8, 160),
  CX41("CX41", 4, 16, 160),
  CPX41("CPX41", 8, 16, 240),
  CX51("CX51", 8, 32, 240),
  CPX51("CPX51", 16, 32, 360);

  private final String name;
  private final int core;
  private final int ram;
  private final int hardDisk;


}
