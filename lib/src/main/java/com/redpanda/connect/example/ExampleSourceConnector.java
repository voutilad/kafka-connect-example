package com.redpanda.connect.example;

import com.redpanda.connect.Common;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExampleSourceConnector extends SourceConnector {

  private static final Logger log = LoggerFactory.getLogger(ExampleSourceConnector.class);
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
    return IntStream.range(0, maxTasks).mapToObj(i -> config.originalsStrings()).collect(Collectors.toList());
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
