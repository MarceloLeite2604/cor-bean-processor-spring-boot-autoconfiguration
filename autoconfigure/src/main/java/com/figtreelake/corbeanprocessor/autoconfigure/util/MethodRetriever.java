package com.figtreelake.corbeanprocessor.autoconfigure.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
public class MethodRetriever {

  public Optional<Method> retrieve(Class<?> clazz, String methodName) {
    try {
      return Optional.of(clazz.getMethod(methodName));
    } catch (NoSuchMethodException noSuchMethodException) {
      final var message = String.format("Exception thrown while searching for method \"%s\" on class \"%s\".", methodName, clazz);
      log.warn(message, noSuchMethodException);
      return Optional.empty();
    }
  }
}
