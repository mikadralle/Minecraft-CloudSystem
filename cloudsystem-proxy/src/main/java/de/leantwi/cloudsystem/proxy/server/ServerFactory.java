package de.leantwi.cloudsystem.proxy.server;

import de.leantwi.cloudsystem.proxy.ProxyConnector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;

@Getter
@Setter
@RequiredArgsConstructor
public class ServerFactory {

    private final ProxyHandler proxyHandler = ProxyConnector.getInstance().getProxyHandler();
    //

    private final String serverName;

    private SessionServer sessionServer;
    private ServerDB serverDB;

    public void registerServer() {
        this.sessionServer = new SessionServer();
        this.sessionServer.fetch(this.serverName);
        //   this.serverDB.fetch(null);
        this.proxyHandler.addServer(this.serverName, this.sessionServer.getHostName(), this.sessionServer.getPort());
    }

    public void unRegisterServer() {
        this.proxyHandler.removeServer(this.serverName);
    }


}
