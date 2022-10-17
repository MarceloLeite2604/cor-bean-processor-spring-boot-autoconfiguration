package com.figtreelake.corbeanprocessor.autoconfigure.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MethodRetrieverTest {

  private MethodRetriever methodRetriever;

  @BeforeEach
  void setUp() {
    methodRetriever = new MethodRetriever();
  }

  @Test
  void shouldReturnOptionalWithMethod() {
    final var methodName = "retrieveValue";
    final var optionalMethod = methodRetriever.retrieve(Dummy.class, methodName);

    assertThat(optionalMethod).isPresent();
  }

  @Test
  void shouldReturnOptionalEmptyWhenMethodIsNotFound() {
    final var methodName = "nonexistentMethod";
    final var optionalMethod = methodRetriever.retrieve(Dummy.class, methodName);

    assertThat(optionalMethod).isEmpty();
  }

  static class Dummy {
    @SuppressWarnings("unused")
    public float retrieveValue() {
      return 2.71828f;
    }
  }
}