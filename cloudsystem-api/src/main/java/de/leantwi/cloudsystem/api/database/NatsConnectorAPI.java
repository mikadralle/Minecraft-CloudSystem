package de.leantwi.cloudsystem.api.database;

import io.nats.client.Connection;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface NatsConnectorAPI {

    void connect();

    void publish(String subject, String message);

    String request(String request,String message) throws ExecutionException, InterruptedException, TimeoutException;

    Connection getConnection();
}
