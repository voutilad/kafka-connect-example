package com.redpanda.connectors.http;

import com.redpanda.connectors.Common;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HttpSinkConnector extends SinkConnector {
  private static final Logger log = LoggerFactory.getLogger(HttpSinkConnector.class);
  private HttpSinkConnectorConfig config = new HttpSinkConnectorConfig(Map.of());


  @Override
  public void start(Map<String, String> props) {
    log.info("start");
    config = new HttpSinkConnectorConfig(props);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return HttpSinkTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    // Nothing special per-task, just share the same config.
    return IntStream.range(0, maxTasks)
        .mapToObj(i -> config.originalsStrings())
        .collect(Collectors.toList());
  }

  @Override
  public void stop() {
    log.info("stopping");
  }

  @Override
  public ConfigDef config() {
    return HttpSinkConnectorConfig.config();
  }

  @Override
  public String version() {
    return Common.VERSION;
  }
}
