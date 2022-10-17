package com.figtreelake.corbeanprocessor.autoconfigure.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassRetrieverTest {

  private ClassRetriever classRetriever;

  @BeforeEach
  void setUp() {
    classRetriever = new ClassRetriever();
  }

  @Test
  void shouldReturnRequestedClass() {
    final var expectedClass = StringBuilder.class;
    final var className = expectedClass.getName();

    final var optionalClass = classRetriever.retrieve(className);

    assertThat(optionalClass).isPresent()
        .contains(expectedClass);
  }

  @Test
  void shouldReturnOptionalEmptyWhenClassIsNotFound() {
    final var className = "unknownClassName";

    final var optionalClass = classRetriever.retrieve(className);

    assertThat(optionalClass).isEmpty();
  }

}