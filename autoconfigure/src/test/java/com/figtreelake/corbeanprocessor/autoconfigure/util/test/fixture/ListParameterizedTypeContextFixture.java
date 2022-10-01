package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.ParameterizedTypeContext;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.DummyInterfaceA;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.DummyInterfaceB;
import lombok.experimental.UtilityClass;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class ListParameterizedTypeContextFixture {

  public static List<ParameterizedTypeContext> createForDummyClassA() {
    return Collections.emptyList();
  }

  public static List<ParameterizedTypeContext> createForDummyClassB() {
    return Collections.emptyList();
  }

  public static List<ParameterizedTypeContext> createForDummyClassC() {

    final ParameterizedType parameterizedTypeInterfaceA = ParameterizedTypeFixture.create(DummyInterfaceA.class);

    final var parameterizedTypeContextInterfaceA = ParameterizedTypeContext.builder()
        .parameterizedType(parameterizedTypeInterfaceA)
        .argument("T", Long.class)
        .build();

    return List.of(parameterizedTypeContextInterfaceA);
  }

  public static List<ParameterizedTypeContext> createForDummyClassD() {

    final ParameterizedType parameterizedTypeInterfaceA = ParameterizedTypeFixture.create(DummyInterfaceA.class);

    final var parameterizedTypeContextInterfaceA = ParameterizedTypeContext.builder()
        .parameterizedType(parameterizedTypeInterfaceA)
        .argument("T", Integer.class)
        .build();

    final ParameterizedType parameterizedTypeInterfaceB = ParameterizedTypeFixture.create(DummyInterfaceB.class);

    final var parameterizedTypeContextInterfaceB = ParameterizedTypeContext.builder()
        .parameterizedType(parameterizedTypeInterfaceB)
        .argument("T", Integer.class)
        .build();

    return List.of(parameterizedTypeContextInterfaceA, parameterizedTypeContextInterfaceB);
  }

  public static List<ParameterizedTypeContext> createForDummyClassE() {

    final ParameterizedType parameterizedTypeInterfaceB = ParameterizedTypeFixture.create(DummyInterfaceB.class);

    final var parameterizedTypeContextInterfaceA = ParameterizedTypeContext.builder()
        .parameterizedType(parameterizedTypeInterfaceB)
        .argument("T", String.class)
        .build();

    return List.of(parameterizedTypeContextInterfaceA);
  }

  public static List<ParameterizedTypeContext> createForDummyClassF() {
    return Collections.emptyList();
  }
}
