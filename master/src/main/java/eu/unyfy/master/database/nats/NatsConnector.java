package eu.unyfy.master.database.nats;

import eu.unyfy.master.MasterBootstrap;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.Getter;

public class NatsConnector {

  private final MasterBootstrap master = MasterBootstrap.getInstance();

  @Getter
  private Connection natsConnection;

  public void connect() {

    final String hostName = MasterBootstrap.getInstance().getConfigAPI().getProperty("nats.hostname");
    final String port = MasterBootstrap.getInstance().getConfigAPI().getProperty("nats.port");
    final String token = MasterBootstrap.getInstance().getConfigAPI().getProperty("nats.token");

    final FutureTask<Connection> task = new FutureTask<>(() -> {
      String url = "nats://" + hostName + ":" + port;

      final Options options = new Options.Builder()
          .server(url)
          .maxReconnects(-1)
          .token(token.toCharArray())
          .build();
      return Nats.connect(options);
    });
    this.master.getExecutorService().execute(task);

    try {
      this.natsConnection = task.get();
      master.sendMessage("nats has been connected successful.");
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  public void sendMessage(String channel, String message) {
    this.natsConnection.publish(channel, message.getBytes(StandardCharsets.UTF_8));
  }

  public String request(String subject, String message) throws InterruptedException, ExecutionException, TimeoutException {
    Future<Message> messageFuture = this.natsConnection.request(subject, message.getBytes(StandardCharsets.UTF_8));
    Message msg = messageFuture.get(500, TimeUnit.MILLISECONDS);
    return new String(msg.getData(), StandardCharsets.UTF_8);
  }
}
