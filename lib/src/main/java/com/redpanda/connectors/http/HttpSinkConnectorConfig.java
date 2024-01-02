package com.redpanda.connectors.http;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class HttpSinkConnectorConfig extends AbstractConfig {
  public final String url;
  public static final String KEY_URL = "http.url";

  public final String tokenUrl;
  public static final String KEY_TOKEN_URL = "http.token.url";

  public final String clientId;
  public static final String KEY_CLIENT_ID = "http.client.id";

  public final String clientSecret;
  public static final String KEY_CLIENT_SECRET = "http.client.secret";

  public final String username;
  public static final String KEY_USERNAME = "http.username";

  public final String password;
  public static final String KEY_PASSWORD = "http.password";

  public HttpSinkConnectorConfig(Map<String, String> props) {
    super(config(), props);

    url = this.getString(KEY_URL);
    tokenUrl = this.getString(KEY_TOKEN_URL);
    clientId = this.getString(KEY_CLIENT_ID);
    clientSecret = this.getString(KEY_CLIENT_SECRET);
    username = this.getString(KEY_USERNAME);
    password = this.getString(KEY_PASSWORD);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(KEY_URL, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH, "URL to POST data to.")
        .define(KEY_TOKEN_URL, ConfigDef.Type.STRING, "", ConfigDef.Importance.MEDIUM, "OAuth2 Token URL")
        .define(KEY_CLIENT_ID, ConfigDef.Type.STRING, "", ConfigDef.Importance.MEDIUM, "OAuth2 Client ID")
        .define(KEY_CLIENT_SECRET, ConfigDef.Type.STRING, "", ConfigDef.Importance.MEDIUM, "OAuth2 Client Secret")
        .define(KEY_USERNAME, ConfigDef.Type.STRING, "", ConfigDef.Importance.MEDIUM, "Username")
        .define(KEY_PASSWORD, ConfigDef.Type.STRING, "", ConfigDef.Importance.MEDIUM, "Password");
  }
}

