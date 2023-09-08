package com.redpanda.connect.example;

import com.redpanda.connect.example.ExampleSourceTask;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ExampleSourceTaskTest {
  @Test
  public void testGeneratesDummyDataTellingTheTime() throws InterruptedException {
    final var task = new ExampleSourceTask();

    // Probably wouldn't test a real poll() call like this, but just an example.
    final List<SourceRecord> data = task.poll();
    Assertions.assertEquals(2, data.size(), "should have 2 dummy records");
    final var head = data.get(0).value().toString();
    Assertions.assertTrue(head.startsWith("The time is now "));
  }
}
