package com.redpanda.connect.example;

import com.redpanda.connect.example.ExampleSinkTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class ExampleSinkTaskTest {
  @Test
  public void simpleExampleSinkTaskTest() {
    final var task = new ExampleSinkTask();
    Assertions.assertDoesNotThrow(() -> task.put(Collections.EMPTY_LIST), "Should not puke.");
  }
}
