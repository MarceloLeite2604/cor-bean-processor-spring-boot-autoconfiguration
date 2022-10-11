package com.figtreelake.corbeanprocessor.autoconfigure.link;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyHighPrecedenceOrderChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyLowPrecedenceOrderChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyLowestPrecedenceOrderChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyAbstractChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.BeanDefinitionFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ChainLinkBeanDefinitionContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ChainLinkBeanDefinitionContextComparatorTest {

  private ChainLinkBeanDefinitionContextComparator chainLinkBeanDefinitionContextComparator;

  @BeforeEach
  void setUp() {
    chainLinkBeanDefinitionContextComparator = new ChainLinkBeanDefinitionContextComparator();
  }

  @ParameterizedTest
  @MethodSource("provideParametersForShouldReturnExpectedValueAccordingToInformedEntriesTest")
  void shouldReturnExpectedValueAccordingToInformedEntries(
      ChainLinkBeanDefinitionContext<? extends ChainLink<?>> firstContext,
      ChainLinkBeanDefinitionContext<? extends ChainLink<?>> secondContext,
      int expectedResult) {
    final var result = chainLinkBeanDefinitionContextComparator.compare(firstContext, secondContext);

    assertThat(result).isEqualTo(expectedResult);
  }

  private static Stream<Arguments> provideParametersForShouldReturnExpectedValueAccordingToInformedEntriesTest() {
    return Stream.of(
        Arguments.of(
            ChainLinkBeanDefinitionContextFixture.create(
                DummyAbstractChainLink.class,
                BeanDefinitionFixture.create(true)),
            ChainLinkBeanDefinitionContextFixture.create(DummyAbstractChainLink.class),
            -1),
        Arguments.of(
            ChainLinkBeanDefinitionContextFixture.create(DummyAbstractChainLink.class),
            ChainLinkBeanDefinitionContextFixture.create(
                DummyAbstractChainLink.class,
                BeanDefinitionFixture.create(true)),
            1),
        Arguments.of(
            ChainLinkBeanDefinitionContextFixture.create(
                DummyAbstractChainLink.class,
                BeanDefinitionFixture.create(true)),
            ChainLinkBeanDefinitionContextFixture.create(
                DummyAbstractChainLink.class,
                BeanDefinitionFixture.create(true)),
            0),
        Arguments.of(
            ChainLinkBeanDefinitionContextFixture.create(DummyLowPrecedenceOrderChainLink.class),
            ChainLinkBeanDefinitionContextFixture.create(DummyAbstractChainLink.class),
            -1),
        Arguments.of(
            ChainLinkBeanDefinitionContextFixture.create(DummyHighPrecedenceOrderChainLink.class),
            ChainLinkBeanDefinitionContextFixture.create(DummyLowPrecedenceOrderChainLink.class),
            -1),
        Arguments.of(
            ChainLinkBeanDefinitionContextFixture.create(DummyLowPrecedenceOrderChainLink.class),
            ChainLinkBeanDefinitionContextFixture.create(DummyHighPrecedenceOrderChainLink.class),
            1),
        Arguments.of(
            ChainLinkBeanDefinitionContextFixture.create(DummyLowestPrecedenceOrderChainLink.class),
            ChainLinkBeanDefinitionContextFixture.create(DummyAbstractChainLink.class),
            0)
    );
  }
}