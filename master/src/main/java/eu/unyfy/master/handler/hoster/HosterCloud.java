package eu.unyfy.master.handler.hoster;

import eu.unyfy.master.MasterBootstrap;
import me.tomsdevsn.hetznercloud.objects.request.ServerRequest;
import me.tomsdevsn.hetznercloud.objects.response.ServerResponse;

public class HosterCloud {


  public void createServer(HetnerType hetnerType, String wrapperID) {

    ServerRequest serverRequest = ServerRequest.builder()
        .name("UN-" + wrapperID.toUpperCase())
        .serverType(hetnerType.getName())
        .location("fsn1")
        .startAfterCreate(true)
        .image("debian-10")
        .build();

    ServerResponse serverResponse = MasterBootstrap.getInstance().getHetznerCloudAPI().createServer(serverRequest);

    MasterBootstrap.getInstance().sendMessage("§eStatus: " + serverResponse.getServer().getStatus());
    MasterBootstrap.getInstance().sendMessage("§eIP: " + serverResponse.getServer().getPublicNet().getIpv4().getIp());
    MasterBootstrap.getInstance().sendMessage("§eStart: " + serverResponse.getAction().getStarted());
    MasterBootstrap.getInstance().sendMessage("§ePassword: " + serverResponse.getRootPassword());
  }

}
