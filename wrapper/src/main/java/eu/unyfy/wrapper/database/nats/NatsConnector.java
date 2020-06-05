package eu.unyfy.wrapper.database.nats;

import eu.unyfy.wrapper.Wrapper;
import eu.unyfy.wrapper.api.config.IniFile;
import eu.unyfy.wrapper.utils.Cache;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import lombok.Getter;


public class NatsConnector {

  private final Wrapper wrapper = Wrapper.getInstance();
  @Getter
  private Connection nats;

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
      this.nats = task.get();
      Cache.sendMessage("nats has been connected!");
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  public void sendMessage(String cloud, String message) {
    this.nats.publish(cloud, message.getBytes(StandardCharsets.UTF_8));
  }


}
