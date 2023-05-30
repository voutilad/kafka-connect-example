package com.redpanda.connect;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class ExampleConnectorConfig extends AbstractConfig {

  public final String username;
  public static final String KEY_USERNAME = "example.username";

  public final String password;
  public static final String KEY_PASSWORD = "example.username";

  public final String topic;
  public static final String KEY_TOPIC = "example.username";


  public ExampleConnectorConfig(Map<String, String> props) {
    super(config(), props);

    username = this.getString(KEY_USERNAME);
    password = this.getString(KEY_PASSWORD);
    topic = this.getString(KEY_TOPIC);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(KEY_USERNAME, ConfigDef.Type.STRING, "D.Fault User", ConfigDef.Importance.HIGH, "A username")
        .define(KEY_PASSWORD, ConfigDef.Type.STRING, "********", ConfigDef.Importance.HIGH, "A password")
        .define(KEY_TOPIC, ConfigDef.Type.STRING, "example-topic", ConfigDef.Importance.MEDIUM, "Target or source topic");
  }
}
