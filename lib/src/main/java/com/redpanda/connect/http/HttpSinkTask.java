package com.redpanda.connect.http;

import com.redpanda.connect.Common;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class HttpSinkTask extends SinkTask {
  private static final Logger log = LoggerFactory.getLogger(HttpSinkTask.class);
  private HttpSinkConnectorConfig config = new HttpSinkConnectorConfig(Map.of());

  private final OkHttpClient client = new OkHttpClient();
  private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
  private String credentials = "";

  @Override
  public String version() {
    return Common.VERSION;
  }

  @Override
  public void start(Map<String, String> props) {
    log.info("start");
    config = new HttpSinkConnectorConfig(props);

    if (!config.tokenUrl.isBlank()) {
      final String token = authenticate(config.tokenUrl, config.clientId, config.clientSecret, config.username, config.password);
      if (token.isBlank())
        throw new RuntimeException("failed to authenticate :(");
      credentials = "Bearer " + token;
    }
  }

  @Override
  public void put(Collection<SinkRecord> records) {
    int good = 0, bad = 0, errors = 0;

    Request.Builder builder = new Request.Builder()
        .url(config.url);

    // XXX does not handle token expiration & renewal
    if (!credentials.isBlank())
        builder = builder.header("Authorization", credentials);

    // Sink the records one at a time, synchronously. (Not the most efficient usage of network i/o.)
    for (final SinkRecord record : records) {
      final String value = record.value().toString();
      final RequestBody body = RequestBody.create(value, JSON);
      final Request request = builder.post(body).build();

      try (final Response response = client.newCall(request).execute()){
        if (response.isSuccessful()) {
          log.info("put record for key {}", record.key());
          good++;
        } else {
          log.warn("failed to put record for key {}", record.key());
          log.debug(response.message());
          bad++;
        }
      } catch (IOException e) {
        log.error("io exception", e);
        errors++;
      }
    }

    log.info("finished put: {} ok, {} bad, {} errors", good, bad, errors);
  }

  @Override
  public void stop() {
    log.info("stop");
  }

  protected String authenticate(String tokenUrl, String clientId, String clientSecret, String username, String password) {
    final RequestBody body = new FormBody.Builder()
        .addEncoded("grant_type", "password")
        .addEncoded("client_id", clientId)
        .addEncoded("client_secret", clientSecret)
        .addEncoded("username", username)
        .addEncoded("password", password)
        .build();

    final Request request = new Request.Builder()
        .url(tokenUrl)
        .post(body)
        .build();

    try (final Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        log.error("authentication failure: " + response.message());
        return "";
      }
      final JSONObject reply = new JSONObject(response.body().string());
      return reply.getString("access_token");

    } catch (IOException e) {
      log.error("io exception", e);
      return "";

    } catch (JSONException e) {
      log.error("failed to find access token in json", e);
      return "";
    }
  }
}
