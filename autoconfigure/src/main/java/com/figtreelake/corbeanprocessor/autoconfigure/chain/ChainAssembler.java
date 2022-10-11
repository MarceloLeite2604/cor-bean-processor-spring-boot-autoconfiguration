package com.figtreelake.corbeanprocessor.autoconfigure.chain;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import org.springframework.util.Assert;

/**
 * Concatenate links to build a chain of responsibility.
 * @author MarceloLeite2604
 */
public class ChainAssembler {

  public <T extends ChainLink<T>> void assemble(Iterable<T> chainLinks) {
    Assert.notNull(chainLinks, "Chain links iterable cannot be null.");

    T previous = null;
    for (T chainLink : chainLinks) {
      if (previous != null) {
        previous.setNext(chainLink);
      }
      previous = chainLink;
    }
  }

  public <X extends ChainLink<X>> void assemble(ChainContext<X> chainContext) {
    Assert.notNull(chainContext, "Chain context cannot be null.");
    assemble(chainContext.getSortedChainLinks());
  }
}
