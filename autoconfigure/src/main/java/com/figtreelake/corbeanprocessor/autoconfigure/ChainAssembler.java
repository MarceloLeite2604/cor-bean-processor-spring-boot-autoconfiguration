package com.figtreelake.corbeanprocessor.autoconfigure;

import org.springframework.util.Assert;

/**
 * Concatenate links to build a chain of responsibility.
 * @author MarceloLeite2604
 */
public class ChainAssembler {

  /**
   * Concatenates chain or responsibility links, setting the next object of
   * each link.
   * @param chainLinkBeanContexts An iterable object containing the context of
   *                              all links that must be chained together
   * @return The context of the first chain link.
   * @param <U> Chain link implementation class/interface.
   */
  public <U extends ChainLink<U>> ChainLinkBeanContext<U> assemble(Iterable<ChainLinkBeanContext<U>> chainLinkBeanContexts) {

    ChainLinkBeanContext<U> first = null;
    ChainLinkBeanContext<U> previous = null;
    for (ChainLinkBeanContext<U> chainBeanContext : chainLinkBeanContexts) {
      if (first == null) {
        first = chainBeanContext;
      } else {
        previous.getBean()
            .setNext(chainBeanContext.getBean());
      }
      previous = chainBeanContext;
    }

    Assert.notNull(first, "Chain link bean contexts cannot be null.");

    return first;
  }
}
