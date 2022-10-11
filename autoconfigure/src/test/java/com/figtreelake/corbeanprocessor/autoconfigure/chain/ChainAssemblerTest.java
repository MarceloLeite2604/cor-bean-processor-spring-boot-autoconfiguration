package com.figtreelake.corbeanprocessor.autoconfigure.chain;


import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyAbstractChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.BeanFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ChainContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChainAssemblerTest {

  private ChainAssembler chainAssembler;

  @BeforeEach
  void setUp() {
    chainAssembler = new ChainAssembler();
  }

  @Test
  void shouldAssembleChainWhenInformingIterableOfChainLinks() {

    final var links = BeanFixture.createBeans(DummyAbstractChainLink.class);

    chainAssembler.assemble(links);

    final var linkIterator = links.iterator();

    final var firstLink = linkIterator.next();
    assertThat(firstLink).isNotNull();

    final var secondLink = linkIterator.next();
    assertThat(firstLink.getNext()).isEqualTo(secondLink);

    final var thirdLink = linkIterator.next();
    assertThat(secondLink.getNext()).isEqualTo(thirdLink);

    final var fourthLink = linkIterator.next();
    assertThat(thirdLink.getNext()).isEqualTo(fourthLink);

    assertThat(fourthLink.getNext()).isNull();
  }

  @Test
  void shouldAssembleChainWhenInformingChainContext() {

    final var chainContext = ChainContextFixture.create(DummyAbstractChainLink.class);

    chainAssembler.assemble(chainContext);

    final var linkIterator = chainContext.getSortedChainLinks().iterator();

    final var firstLink = linkIterator.next();
    assertThat(firstLink).isNotNull();

    final var secondLink = linkIterator.next();
    assertThat(firstLink.getNext()).isEqualTo(secondLink);

    final var thirdLink = linkIterator.next();
    assertThat(secondLink.getNext()).isEqualTo(thirdLink);

    final var fourthLink = linkIterator.next();
    assertThat(thirdLink.getNext()).isEqualTo(fourthLink);

    assertThat(fourthLink.getNext()).isNull();
  }

  @Test
  void shouldThrowIllegalStateExceptionWhenChainLinkIterableIsNull() {
    assertThrows(IllegalArgumentException.class, () -> chainAssembler.assemble((Iterable<DummyAbstractChainLink>) null));
  }

  @Test
  void shouldThrowIllegalStateExceptionWhenChainContextIsNull() {
    assertThrows(IllegalArgumentException.class, () -> chainAssembler.assemble((ChainContext<DummyAbstractChainLink>) null));
  }
}