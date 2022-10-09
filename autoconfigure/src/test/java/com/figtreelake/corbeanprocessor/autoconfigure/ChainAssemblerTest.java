package com.figtreelake.corbeanprocessor.autoconfigure;


import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.*;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ChainLinkBeanContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChainAssemblerTest {

  private ChainAssembler chainAssembler;

  @BeforeEach
  void setUp() {
    chainAssembler = new ChainAssembler();
  }

  @Test
  void shouldAssembleChain() {

    final var dummyChainLinkA = new DummyChainLinkA();
    final var dummyChainLinkABeanContext = ChainLinkBeanContextFixture.create(dummyChainLinkA);

    final var dummyChainLinkB = new DummyChainLinkB();
    final var dummyChainLinkBBeanContext = ChainLinkBeanContextFixture.create(dummyChainLinkB);

    final var dummyChainLinkC = new DummyChainLinkC();
    final var dummyChainLinkCBeanContext = ChainLinkBeanContextFixture.create(dummyChainLinkC);

    final var dummyChainLinkD = new DummyChainLinkD();
    final var dummyChainLinkDBeanContext = ChainLinkBeanContextFixture.create(dummyChainLinkD);

    final var chainLinks = Set.of(
        dummyChainLinkA,
        dummyChainLinkB,
        dummyChainLinkC,
        dummyChainLinkD);

    final var chainLinkBeanContexts = Set.of(
        dummyChainLinkABeanContext,
        dummyChainLinkBBeanContext,
        dummyChainLinkCBeanContext,
        dummyChainLinkDBeanContext);

    final var firstDummyChainLinkBeanContext = chainAssembler.assemble(chainLinkBeanContexts);

    assertThat(firstDummyChainLinkBeanContext).isIn(chainLinkBeanContexts);

    final var linksAlreadyFound = new LinkedList<>();

    DummyAbstractChainLink firstLink = firstDummyChainLinkBeanContext.getBeanDefinition();
    assertThat(firstLink).isIn(chainLinks);

    linksAlreadyFound.add(firstLink);

    DummyAbstractChainLink secondLink = firstLink.getNext();
    assertThat(secondLink).isIn(chainLinks)
        .isNotIn(linksAlreadyFound);

    linksAlreadyFound.add(secondLink);

    DummyAbstractChainLink thirdLink = secondLink.getNext();
    assertThat(thirdLink).isIn(chainLinks)
        .isNotIn(linksAlreadyFound);

    linksAlreadyFound.add(thirdLink);

    DummyAbstractChainLink fourthLink = thirdLink.getNext();
    assertThat(fourthLink).isIn(chainLinks)
        .isNotIn(linksAlreadyFound);

    assertThat(fourthLink.getNext()).isNull();
  }

  @Test
  void shouldThrowIllegalStateExceptionWhenChainLinkBeanContextIterableIsNull() {

    final Set<ChainLinkBeanDefinitionContext<DummyAbstractChainLink>> chainLinkBeanDefinitionContextIterable = Collections.emptySet();
    assertThrows(IllegalArgumentException.class, () -> chainAssembler.assemble(chainLinkBeanDefinitionContextIterable));
  }
}