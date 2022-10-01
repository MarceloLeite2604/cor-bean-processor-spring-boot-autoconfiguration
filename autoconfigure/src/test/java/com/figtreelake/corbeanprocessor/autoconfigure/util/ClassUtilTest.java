package com.figtreelake.corbeanprocessor.autoconfigure.util;

import com.figtreelake.corbeanprocessor.autoconfigure.ParameterizedTypeContext;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.*;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ListParameterizedTypeContextFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.ParameterizedTypeEqualsPredicate;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ClassUtilTest {

  private ClassUtil classUtil;

  @BeforeEach
  void setUp() {
    classUtil = new ClassUtil();
  }

  @ParameterizedTest
  @MethodSource("provideParametersForShouldReturnAllGenericInterfacesTest")
  void shouldReturnAllGenericInterfaces(Class<?> inputClass, List<ParameterizedTypeContext> expectedParameterizedTypeContexts) {
    final var genericInterfaceContexts = classUtil.retrieveGenericInterfacesForClass(inputClass);

    RecursiveComparisonConfiguration recursiveComparisonConfiguration = RecursiveComparisonConfiguration.builder()
        .withEqualsForType(new ParameterizedTypeEqualsPredicate(), ParameterizedType.class)
        .build();


    assertThat(genericInterfaceContexts).usingRecursiveFieldByFieldElementComparator(recursiveComparisonConfiguration)
        .containsExactlyElementsOf(expectedParameterizedTypeContexts);

  }

  private static Stream<Arguments> provideParametersForShouldReturnAllGenericInterfacesTest() {
    return Stream.of(
        Arguments.of(DummyClassA.class, ListParameterizedTypeContextFixture.createForDummyClassA()),
        Arguments.of(DummyClassB.class, ListParameterizedTypeContextFixture.createForDummyClassB()),
        Arguments.of(DummyClassC.class, ListParameterizedTypeContextFixture.createForDummyClassC()),
        Arguments.of(DummyClassD.class, ListParameterizedTypeContextFixture.createForDummyClassD()),
        Arguments.of(DummyClassE.class, ListParameterizedTypeContextFixture.createForDummyClassE()),
        Arguments.of(DummyClassF.class, ListParameterizedTypeContextFixture.createForDummyClassF())
    );
  }

}