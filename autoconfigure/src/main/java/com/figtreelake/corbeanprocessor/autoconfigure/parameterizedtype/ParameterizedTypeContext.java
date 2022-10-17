package com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Stores information about a generic type implemented by a class.
 * @author MarceloLeite2604
 */
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ParameterizedTypeContext {

  private final ParameterizedType parameterizedType;

  @Singular
  private final Map<String, Class<?>> arguments;
}
