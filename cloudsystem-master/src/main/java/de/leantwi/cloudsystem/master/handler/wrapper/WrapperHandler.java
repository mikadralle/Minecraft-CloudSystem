package de.leantwi.cloudsystem.master.handler.wrapper;

import de.leantwi.cloudsystem.master.handler.hoster.HetnerType;
import de.leantwi.cloudsystem.master.handler.wrapper.handler.StartWrapperHandler;
import de.leantwi.cloudsystem.master.MasterBootstrap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lombok.Getter;

@Getter
public class WrapperHandler {

    private final MasterBootstrap masterBootstrap = MasterBootstrap.getInstance();
    private final Map<String, String> publicIP = new HashMap<>();
    private final Map<String, WrapperServer> wrapperServerMap = new HashMap<>();
    private final List<String> wrapperList = new ArrayList<>();

    public String verifyWrapper(String wrapperID, WrapperType wrapperType, int weightClass, int priority) {

        WrapperServer wrapperServer = new WrapperServer();
        wrapperServer.fetch(wrapperID, publicIP.get(wrapperID), wrapperType, weightClass, priority);
        this.wrapperServerMap.put(wrapperID, wrapperServer);
        this.removePublicIP(wrapperID);
        masterBootstrap.sendMessage("Hostname: " + wrapperServer.getHostName());
        return wrapperServer.getHostName();

    }

    public void logoutWrapper(String wrapperName) {
        this.wrapperServerMap.remove(wrapperName);
        this.masterBootstrap.sendMessage("The Wrapper " + wrapperName + " has been disconnected from master server!");
        this.wrapperList.remove(wrapperName);
    }


    public String getRandomWrapper(String subGroupName, int weightClass) {

        if (getWrapperServer("wrapper-1").isFreeSlot(subGroupName)) {
            System.out.println("true");
            return "wrapper-1";
        }

        List<WrapperServer> wrapperServerList = new ArrayList<>();
        wrapperServerList.addAll(this.wrapperServerMap.values());
        wrapperServerList.remove(this.getWrapperServer("wrapper-1"));
        wrapperServerList.sort(Comparator.comparingInt(WrapperServer::getPriority));

        if (wrapperServerList.stream().anyMatch(wrapper -> wrapper.getWeightClass() > weightClass)) {
            return wrapperServerList.stream().filter(wrapper -> wrapper.getWeightClass() > weightClass).findAny().get().getWrapperID();
        }

        startNewWrapper();
        return null;
    }

    public void addPublicIP(String wrapperID, String hostName) {
        masterBootstrap.sendMessage("ADD ADDRESS: " + hostName);
        this.wrapperList.add(wrapperID);
        this.publicIP.put(wrapperID, hostName);
    }

    public void removePublicIP(String wrapperID) {
        this.publicIP.remove(wrapperID);
    }

    private void startNewWrapper() {
        createServer(HetnerType.CPX21);
    }

    public boolean isWrapperOnline() {
        return !this.wrapperServerMap.isEmpty();
    }

    public int getID() {

        for (int i = 2; i < 999; i++) {
            if (!this.wrapperList.contains("wrapper-" + i)) {
                return i;
            }
        }
        return this.wrapperList.size() + 1;
    }


    public void createServer(HetnerType hetnerType) {

        String wrapperID = "wrapper-" + MasterBootstrap.getInstance().getWrapperHandler().getID();

 /*   ServerRequest serverRequest = ServerRequest.builder()
        .name(wrapperID)
        .serverType(hetnerType.getName())
        .location(this.getDataCenter())
        .startAfterCreate(true)
        .image("17774648")
        .network(101222L)
        .sshKeys(this.getSSHKeys())
        .build();

    MasterBootstrap.getInstance().getLogger().info("Master will be started new wrapper called " + wrapperID);
    ServerResponse serverResponse = MasterBootstrap.getInstance().getHetznerCloudAPI().createServer(serverRequest);
    serverResponse.getServer().getId();
    serverResponse.getAction().setCommand("mkdir /home/fynn/");

    this.addPublicIP(wrapperID, serverResponse.getServer().getPublicNet().getIpv4().toString());

    MasterBootstrap.getInstance().sendMessage("§eStatus: " + serverResponse.getServer().getStatus());
    MasterBootstrap.getInstance().sendMessage("§eIP: " + serverResponse.getServer().getPublicNet().getIpv4().getIp());
    MasterBootstrap.getInstance().sendMessage("§eStart: " + serverResponse.getAction().getStarted());
    MasterBootstrap.getInstance().sendMessage("§ePassword: " + serverResponse.getRootPassword());

    MasterBootstrap.getInstance().getExecutorService().execute(new StartWrapperHandler(serverResponse.getServer().getId()));

*/
    }

    public WrapperServer getWrapperServer(String wrapperName) {
        return this.wrapperServerMap.get(wrapperName);
    }


    public String getDataCenter() {

        List<String> list = new ArrayList<>();
        list.add("fsn1");
        list.add("nbg1");
        return list.get(new Random().nextInt(list.size()));
    }


    private List<Long> getSSHKeys() {
    /*
    List<Long> sshKey = new ArrayList<>();
    MasterBootstrap.getInstance().getHetznerCloudAPI().getSSHKeys().getSshKeys().forEach(sshKeys -> {
      sshKey.add(sshKeys.getId());
    });


    return sshKey;
 */
        return null;
    }
}
