package com.figtreelake.corbeanprocessor.autoconfigure.chain;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import org.springframework.util.Assert;

/**
 * Concatenate links to build a chain of responsibility.
 *
 * @author MarceloLeite2604
 */
public class ChainAssembler {

  /**
   * Assemble links to create a chain.
   *
   * @param chainLinks Chain links to be concatenated.
   * @param <T>        Element that either implements or extends the {@link ChainLink}
   *           interface.
   */
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

  /**
   * Assemble links to create a chain.
   *
   * @param chainContext The context of the chain to be assembled.
   * @param <T>          Element that either implements or extends the {@link ChainLink} interface.
   */
  public <T extends ChainLink<T>> void assemble(ChainContext<T> chainContext) {
    Assert.notNull(chainContext, "Chain context cannot be null.");
    assemble(chainContext.retrieveChainLinks());
  }
}
