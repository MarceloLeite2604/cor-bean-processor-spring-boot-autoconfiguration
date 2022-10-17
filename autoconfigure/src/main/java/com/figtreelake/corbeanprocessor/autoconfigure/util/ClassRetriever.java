package com.figtreelake.corbeanprocessor.autoconfigure.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ClassRetriever {

  public Optional<Class<?>> retrieve(String className) {
    try {
      return Optional.of(Class.forName(className));
    } catch (ClassNotFoundException classNotFoundException) {
      final var message = String.format("Exception thrown while searching class \"%s\".", className);
      log.warn(message, classNotFoundException);
      return Optional.empty();
    }
  }
}
