package eu.unyfy.master.database.nats;

import eu.unyfy.master.Master;
import eu.unyfy.master.api.config.IniFile;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import lombok.Getter;

public class NatsConnector {

  private final Master master = Master.getInstance();
  private final IniFile configAPI = this.master.getConfigAPI().getConfigAPI();
  @Getter
  private Connection natsConnection;


  public void connect() {

    final String hostName = configAPI.getProperty("nats.hostname");
    final String port = configAPI.getProperty("nats.port");
    final String token = configAPI.getProperty("nats.token");

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
      Master.getInstance().getConsole().sendMessage("nats has been connected.");
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  public void sendMessage(String channel, String message) {
    System.out.println("publish: " + channel + "=" + message);
    this.natsConnection.publish(channel, message.getBytes(StandardCharsets.UTF_8));
  }

}
