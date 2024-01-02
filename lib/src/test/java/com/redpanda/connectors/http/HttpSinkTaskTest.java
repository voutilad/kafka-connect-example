package com.redpanda.connectors.http;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpSinkTaskTest {
  private static final Logger log = LoggerFactory.getLogger(HttpSinkTaskTest.class);
  public static final String tokenUrl = System.getenv().getOrDefault("TOKEN_URL", "");
  public static final String clientSecret = System.getenv().getOrDefault("CLIENT_SECRET", "");
  public static final String clientId = System.getenv().getOrDefault("CLIENT_ID", "");
  public static final String username = System.getenv().getOrDefault("USERNAME", "");
  public static final String password = System.getenv().getOrDefault("PASSWORD", "");

  private static final MockWebServer server = new MockWebServer();
  private static HttpSinkConnectorConfig config;
  @BeforeAll
  public static void beforeAll() throws IOException {
    server.start();
    log.info("started mock webserver: {}", server);

    final String url = server.url("/fake").url().toString();
    log.info("using mock url: {}", url);

    config = new HttpSinkConnectorConfig(Map.of(
        HttpSinkConnectorConfig.KEY_URL, url,
        HttpSinkConnectorConfig.KEY_CLIENT_ID, clientId,
        HttpSinkConnectorConfig.KEY_CLIENT_SECRET, clientSecret,
        HttpSinkConnectorConfig.KEY_TOKEN_URL, tokenUrl,
        HttpSinkConnectorConfig.KEY_USERNAME, username,
        HttpSinkConnectorConfig.KEY_PASSWORD, password
    ));
  }

  @Test
  public void testHttpSinkTask() {
    final HttpSinkTask task = new HttpSinkTask();

    Assertions.assertDoesNotThrow(() -> task.start(config.originalsStrings()),
        "should start sink task without authentication error");

    // Sink a few fake records.
    var records = List.of(
        new SinkRecord("fake-topic", 1, Schema.STRING_SCHEMA, "fake-key-1",
            Schema.STRING_SCHEMA, "{\"name\": \"dave1\"}", 1),
        new SinkRecord("fake-topic", 1, Schema.STRING_SCHEMA, "fake-key-2",
            Schema.STRING_SCHEMA, "{\"name\": \"dave2\"}", 2)
    );
    for (int i = 0; i < records.size(); i++)
      server.enqueue(new MockResponse().setResponseCode(200).setBody("{ \"result\": \"OK\"}"));

    Assertions.assertDoesNotThrow(() -> task.put(records),
        "should sink data without uncaught exception");

    Assertions.assertDoesNotThrow(task::stop,
        "should stop without exception");

    Assertions.assertEquals(2, server.getRequestCount(), "should have one request for each SinkRecord");
  }

  @AfterAll
  public static void afterAll() throws IOException {
    server.shutdown();
  }
}
