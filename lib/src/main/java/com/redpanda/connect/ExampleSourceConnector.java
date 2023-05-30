package com.redpanda.connect;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExampleSourceConnector extends SourceConnector {

  private static final Logger log = Logger.getLogger(ExampleSourceConnector.class.getName());
  private ExampleConnectorConfig config = new ExampleConnectorConfig(Map.of());

  @Override
  public void start(Map<String, String> props) {
    log.info("start() called");
    config = new ExampleConnectorConfig(props);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return ExampleSourceTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    // This would normally generate maxTasks-distinct configs. We fake it.
    return IntStream.range(1, maxTasks).mapToObj(i -> config.originalsStrings()).collect(Collectors.toList());
  }

  @Override
  public void stop() {
    log.info("stop() called");
  }

  @Override
  public ConfigDef config() {
    return ExampleConnectorConfig.config();
  }

  @Override
  public String version() {
    return Common.VERSION;
  }
}