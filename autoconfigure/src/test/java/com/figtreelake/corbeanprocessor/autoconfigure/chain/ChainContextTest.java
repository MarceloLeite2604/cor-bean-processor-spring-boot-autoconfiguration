package com.figtreelake.corbeanprocessor.autoconfigure.chain;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyAbstractChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyChainLinkA;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ChainContextFixture;
import org.junit.jupiter.api.Test;

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

}