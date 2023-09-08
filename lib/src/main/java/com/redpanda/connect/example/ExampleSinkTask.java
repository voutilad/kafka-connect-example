package com.redpanda.connect.example;

import com.redpanda.connect.Common;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

public class ExampleSinkTask extends SinkTask {

  private static final Logger log = LoggerFactory.getLogger(ExampleSinkTask.class);
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
