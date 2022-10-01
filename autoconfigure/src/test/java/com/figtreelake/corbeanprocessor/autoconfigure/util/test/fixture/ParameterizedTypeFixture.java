package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.DummyInterfaceA;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.DummyInterfaceB;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.ParameterizedTypeForTests;
import lombok.experimental.UtilityClass;

import java.lang.reflect.ParameterizedType;

@UtilityClass
public class ParameterizedTypeFixture {

  public ParameterizedType create(Class<?> rawType) {
    return ParameterizedTypeForTests.builder()
        .rawType(rawType)
        .build();
  }
}
