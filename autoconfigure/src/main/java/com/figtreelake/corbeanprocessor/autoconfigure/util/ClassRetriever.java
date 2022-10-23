package com.figtreelake.corbeanprocessor.autoconfigure.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Retrieve class details as {@link Class} object.
 *
 * @author MarceloLeite2604
 */
@Slf4j
public class ClassRetriever {

  /**
   * Retrieve class details as {@link Class} object
   *
   * @param className The class name
   * @return An {@link Optional} containing the class details as {@link Class}
   * object or empty if the class was not found.
   */
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
