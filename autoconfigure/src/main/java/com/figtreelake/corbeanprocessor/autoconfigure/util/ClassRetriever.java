package com.figtreelake.corbeanprocessor.autoconfigure.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ClassRetriever {

  private static ClassRetriever instance;

  public static ClassRetriever getInstance() {
    if (instance == null) {
      instance = new ClassRetriever();
    }
    return instance;
  }

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
