package com.figtreelake.corbeanprocessor.autoconfigure.chain;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyAbstractChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyChainLinkA;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.BeanFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ChainContextFixture;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class ChainContextTest {


  @Test
  void shouldAddChainLinkBean() {
    final var chainContext = ChainContextFixture.create(DummyAbstractChainLink.class);

    final var beanName = "beanNameValue";

    final var bean = new DummyChainLinkA();

    chainContext.addChainLinkBean(beanName, bean);

    assertThat(chainContext.getChainLinkBeansByName()).containsEntry(beanName, bean);
  }

  @Test
  void shouldReturnTrueWhenHasAllChainLinks() {
    final var chainContext = ChainContextFixture.create(DummyAbstractChainLink.class);

    final var result = chainContext.hasAllChainLinks();

    assertThat(result).isTrue();
  }

  @Test
  void shouldReturnFalseWhenDoesNotHaveAllChainLinks() {
    final var chainContext = ChainContextFixture.create(DummyAbstractChainLink.class)
        .toBuilder()
        .chainLinkBeansByName(new HashMap<>())
        .build();

    final var result = chainContext.hasAllChainLinks();

    assertThat(result).isFalse();
  }

  @Test
  void shouldReturnAllChainLinksSorted() {
    final var chainContext = ChainContextFixture.create(DummyAbstractChainLink.class);

    final var expectedSortedChainLinks = BeanFixture.createBeans(DummyAbstractChainLink.class);

    final var sortedChainLinks = chainContext.getSortedChainLinks();

    assertThat(sortedChainLinks).containsExactlyElementsOf(expectedSortedChainLinks);
  }

  @Test
  void shouldInstantiateChainLinkBeansByNameWhenFieldIsNull() {
    final var chainContext = ChainContextFixture.create(DummyAbstractChainLink.class)
        .toBuilder()
        .chainLinkBeansByName(null)
        .build();

    final var chainLinkBeansByName = chainContext.getChainLinkBeansByName();

    assertThat(chainLinkBeansByName).isNotNull();
  }

}