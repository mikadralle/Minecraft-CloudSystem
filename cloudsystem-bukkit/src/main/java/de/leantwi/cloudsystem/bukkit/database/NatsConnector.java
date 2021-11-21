package de.leantwi.cloudsystem.bukkit.database;

import de.leantwi.cloudsystem.bukkit.BukkitConnector;
import de.leantwi.cloudsystem.bukkit.config.IniFile;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class NatsConnector {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Getter
    private Connection nats;
    private IniFile configAPI = BukkitConnector.getInstance().getConfigAPI();
    public void connect() {

        final String host = this.configAPI.getProperty("nats.host");
        final String port = this.configAPI.getProperty("nats.port");
        final String token = this.configAPI.getProperty("nats.token");

        final FutureTask<Connection> task = new FutureTask<>(() -> {
            final String natsURL = "nats://" + host + ":" + port;

            final Options options = new Options.Builder()
                    .server(natsURL)
                    .maxReconnects(-1)
                    .token(token.toCharArray())
                    .build();

            return Nats.connect(options);
        });
        //start thread
        this.executorService.execute(task);
        try {
            this.nats = task.get();
            Bukkit.getConsoleSender().sendMessage("nats has been connected.");
        } catch (InterruptedException | ExecutionException e) {
            Bukkit.getConsoleSender().sendMessage("nats couldn't connect " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void publishMessage(String subject, String message) {
        this.nats.publish(subject, message.getBytes(StandardCharsets.UTF_8));
    }

    public String request(String subject, String message) throws InterruptedException, ExecutionException, TimeoutException {
        Future<Message> messageFuture = this.nats.request(subject, message.getBytes(StandardCharsets.UTF_8));
        Message msg = messageFuture.get(500, TimeUnit.MILLISECONDS);
        return new String(msg.getData(), StandardCharsets.UTF_8);
    }
}
