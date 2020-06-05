package eu.unyfy.wrapper.database.nats;

import eu.unyfy.wrapper.Wrapper;
import eu.unyfy.wrapper.api.config.IniFile;
import eu.unyfy.wrapper.utils.Cache;
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

  private final Wrapper wrapper = Wrapper.getInstance();
  @Getter
  private Connection natsConnection;

  public void connect() {

    final IniFile configAPI = this.wrapper.getConfigAPI();
    final String hostName = configAPI.getProperty("nats.hostname");
    final String port = configAPI.getProperty("nats.port");
    final String token = configAPI.getProperty("nats.token");

    final FutureTask<Connection> task = new FutureTask<>(() -> {

      final String url = "nats://" + hostName + ":" + port;
      final Options options = new Options.Builder()
          .server(url)
          .token(token.toCharArray())
          .maxReconnects(-1)
          .build();
      return Nats.connect(options);
    });

    this.wrapper.getExecutorService().execute(task);
    try {
      this.natsConnection = task.get();
      Cache.sendMessage("nats has been connected!");
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  public void sendMessage(String cloud, String message) {
    this.natsConnection.publish(cloud, message.getBytes(StandardCharsets.UTF_8));
  }

  public String request(String subject, String message) throws InterruptedException, ExecutionException, TimeoutException {
    Future<Message> messageFuture = this.natsConnection.request(subject, message.getBytes(StandardCharsets.UTF_8));
    Message msg = messageFuture.get(500, TimeUnit.MILLISECONDS);
    return new String(msg.getData(), StandardCharsets.UTF_8);
  }

}
