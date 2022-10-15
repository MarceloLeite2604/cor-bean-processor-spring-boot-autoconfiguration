package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation;

import lombok.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ParameterizedTypeTestImplementation implements ParameterizedType {

  @Getter
  private final Type rawType;

  @Getter
  private final Type ownerType;

  @Override
  public Type[] getActualTypeArguments() {
    return new Type[0];
  }
}
