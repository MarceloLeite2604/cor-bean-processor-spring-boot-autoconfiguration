package com.figtreelake.corbeanprocessor.autoconfigure;

import lombok.*;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Getter
public class ParameterizedTypeContext {

  private final ParameterizedType parameterizedType;

  @Singular
  private final Map<String, Class<?>> arguments;
}
