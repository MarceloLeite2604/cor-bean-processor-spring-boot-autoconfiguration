package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.ParameterizedTypeTestImplementation;
import lombok.experimental.UtilityClass;

import java.lang.reflect.ParameterizedType;

@UtilityClass
public class ParameterizedTypeFixture {

  public ParameterizedType create(Class<?> rawType) {
    return ParameterizedTypeTestImplementation.builder()
        .rawType(rawType)
        .build();
  }
}
