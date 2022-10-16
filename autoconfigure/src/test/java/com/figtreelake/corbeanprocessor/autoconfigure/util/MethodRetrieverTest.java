package com.figtreelake.corbeanprocessor.autoconfigure.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MethodRetrieverTest {

  @Test
  void shouldReturnSameMethodRetrieverInstance() {
    final var methodRetriever = MethodRetriever.getInstance();

    assertThat(methodRetriever).isInstanceOf(MethodRetriever.class);

    final var anotherMethodRetriever = MethodRetriever.getInstance();

    assertThat(anotherMethodRetriever).isEqualTo(methodRetriever);
  }

  @Test
  void shouldReturnOptionalWithMethod() {
    final var methodName = "retrieveValue";
    final var optionalMethod = MethodRetriever.getInstance()
        .retrieve(Dummy.class, methodName);

    assertThat(optionalMethod).isPresent();
  }

  @Test
  void shouldReturnOptionalEmptyWhenMethodIsNotFound() {
    final var methodName = "nonexistentMethod";
    final var optionalMethod = MethodRetriever.getInstance()
        .retrieve(Dummy.class, methodName);

    assertThat(optionalMethod).isEmpty();
  }

  static class Dummy {
    @SuppressWarnings("unused")
    public float retrieveValue() {
      return 2.71828f;
    }
  }
}