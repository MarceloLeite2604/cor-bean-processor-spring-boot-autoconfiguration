package com.figtreelake.corbeanprocessor.autoconfigure.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Retrieve class methods details as {@link Method} object.
 *
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
    final var optionalMethod = Arrays.stream(clazz.getMethods())
        .filter(method -> methodName.equals(method.getName()))
        .findFirst();

    if (optionalMethod.isPresent()) {
      log.debug("Could not find method \"{}\" on class \"{}\".", methodName, clazz);
    }

    return optionalMethod;
  }
}
