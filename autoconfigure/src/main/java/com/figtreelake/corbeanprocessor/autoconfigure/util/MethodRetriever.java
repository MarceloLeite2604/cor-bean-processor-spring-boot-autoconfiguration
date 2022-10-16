package com.figtreelake.corbeanprocessor.autoconfigure.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class MethodRetriever {

  private static MethodRetriever instance;

  public static MethodRetriever getInstance() {
    if (instance == null) {
      instance = new MethodRetriever();
    }
    return instance;
  }

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
