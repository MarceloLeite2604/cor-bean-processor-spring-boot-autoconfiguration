package com.figtreelake.corbeanprocessor.autoconfigure;

import org.springframework.util.Assert;

public class ChainAssembler {

  public <U extends ChainLink<U>, T extends ChainLinkBeanContext<U>> T assemble(Iterable<T> chainLinkBeanContexts) {

    T first = null;
    T previous = null;
    for (T chainBeanContext : chainLinkBeanContexts) {
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
