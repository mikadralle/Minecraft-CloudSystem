package de.leantwi.cloudsystem.proxy.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import de.leantwi.cloudsystem.proxy.ProxyConnector;
import de.leantwi.cloudsystem.proxy.config.IniFile;
import net.md_5.bungee.api.ProxyServer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDBConnector {


    private final IniFile configAPI = ProxyConnector.getInstance().getConfigAPI();

    private MongoClient mongoClient;

    public void connect() {

        final String mongo_host = configAPI.getProperty("mongoDB.host");
        final String mongo_auth_db = configAPI.getProperty("mongoDB.authDB");
        final String mongo_user = configAPI.getProperty("mongoDB.user");
        final String mongo_pass = configAPI.getProperty("mongoDB.password");

        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        final MongoCredential credential = MongoCredential.createCredential(mongo_user, mongo_auth_db, mongo_pass.toCharArray());
        final MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).connectionsPerHost(2000).build();
        this.mongoClient = new MongoClient(new ServerAddress(mongo_host), credential, options);
        ProxyServer.getInstance().getConsole().sendMessage("MongoDB has been connected");

    }

    public void disconnect() {

        this.mongoClient.close();
        ProxyServer.getInstance().getConsole().sendMessage("MongoDB has been disconnected from the database server.");

    }

}
