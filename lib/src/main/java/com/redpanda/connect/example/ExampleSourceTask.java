package com.redpanda.connect.example;

import com.redpanda.connect.Common;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ExampleSourceTask extends SourceTask {
  private static final Logger log = LoggerFactory.getLogger(ExampleSourceTask.class);
  private static final AtomicInteger cnt = new AtomicInteger(0);
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

  public static SourceRecord makeSourceRecord(String topic, String key, String value) {
    return new SourceRecord(
        Map.of("thread", Thread.currentThread().getName()), // fake "Source Partition"
        Map.of("offset", System.currentTimeMillis()), // fake "Source Offset"
        topic,
        Schema.STRING_SCHEMA, // key schema
        key,
        Schema.STRING_SCHEMA, // value schema
        value
    );

  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    log.info("poll() called");
    final String fakeKey = Thread.currentThread().getName();

    return List.of(
        makeSourceRecord(config.topic, fakeKey, String.format("The time is now %d", System.currentTimeMillis())),
        makeSourceRecord(config.topic, fakeKey, String.format("cnt is now %d", cnt.getAndIncrement()))
    );
  }

  @Override
  public void stop() {
    log.info("stop() called");
  }
}
