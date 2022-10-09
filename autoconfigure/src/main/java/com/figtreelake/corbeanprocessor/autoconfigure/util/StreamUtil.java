package com.figtreelake.corbeanprocessor.autoconfigure.util;

import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@UtilityClass
public class StreamUtil {

  public static <T> Stream<T> getOptionalIfPresent(Optional<T> optionalStream) {
    return Stream.of(optionalStream)
        .filter(Optional::isPresent)
        .map(Optional::get);
  }
}
