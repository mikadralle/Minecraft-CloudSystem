package eu.unyfy.master;

public class LauncherBootStrap {

  private static MasterBootstrap masterBootstrap;

  public static void main(String[] strings) {

    masterBootstrap = new MasterBootstrap();


  }

  public static MasterBootstrap getMasterBootstrap() {
    return masterBootstrap;
  }

}
