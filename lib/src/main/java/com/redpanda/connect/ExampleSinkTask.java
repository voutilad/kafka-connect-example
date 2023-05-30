package com.redpanda.connect;

import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

public class ExampleSinkTask extends SinkTask {

  private static final Logger log = Logger.getLogger(ExampleSinkTask.class.getName());
  private ExampleConnectorConfig config = new ExampleConnectorConfig(Map.of());

  @Override
  public String version() {
    return Common.VERSION;
  }

  @Override
  public void start(Map<String, String> props) {
    log.info("start() called");
    config = new ExampleConnectorConfig(props);
  }

  @Override
  public void put(Collection<SinkRecord> records) {
    for (SinkRecord record : records) {
      log.info("put: " + record.toString());
    }
  }

  @Override
  public void stop() {
    log.info("stop() called");
  }
}
