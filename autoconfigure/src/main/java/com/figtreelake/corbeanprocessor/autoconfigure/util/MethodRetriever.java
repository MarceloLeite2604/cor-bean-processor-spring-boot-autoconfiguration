package com.figtreelake.corbeanprocessor.autoconfigure.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Retrieve class methods details as {@link Method} object.
 * @author MarceloLeite2604
 */
@Slf4j
public class MethodRetriever {

  /**
   * Retrieve method details as {@link Method} object
   *
   * @param clazz      Class which contains the method.
   * @param methodName Method name to search.
   * @return An {@link Optional} containing the method description or empty if the method
   * was not found.
   */
  public Optional<Method> retrieve(Class<?> clazz, String methodName) {
    try {
      return Optional.of(clazz.getMethod(methodName));
    } catch (NoSuchMethodException noSuchMethodException) {
      final var message = String.format("Exception thrown while searching for method \"%s\" on class \"%s\".", methodName, clazz);
      log.debug(message, noSuchMethodException);
      return Optional.empty();
    }
  }
}
