package de.leantwi.cloudsystem.database;

import de.leantwi.cloudsystem.api.database.NatsConnectorAPI;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

@RequiredArgsConstructor
public class NatsConnector implements NatsConnectorAPI {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    public Connection connection;
    private final String hostName, token;
    private final int port;

    @Override
    public void connect() {

        final FutureTask<Connection> task = new FutureTask<>(() -> {
            final String natsURL = "nats://" + hostName + ":" + port;

            final Options options = new Options.Builder()
                    .server(natsURL)
                    .maxReconnects(-1)
                    .token(token.toCharArray())
                    .build();
            return Nats.connect(options);
        });

        this.executorService.execute(task);
        System.out.println("Nats has been connected.");

        try {
            this.connection = task.get();
            //TODO: Logger
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void publish(String subject, String message) {
        this.connection.publish(subject,message.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String request(String subject, String message) throws ExecutionException, InterruptedException, TimeoutException {
        Future<Message> messageFuture = this.connection.request(subject, message.getBytes(StandardCharsets.UTF_8));
        Message msg = messageFuture.get(500, TimeUnit.MILLISECONDS);
        return new String(msg.getData(), StandardCharsets.UTF_8);
    }

    public Connection getConnection() {
        return connection;
    }
}
