package com.figtreelake.corbeanprocessor.autoconfigure;

/**
 * Concatenate links to build a chain of responsibility.
 * @author MarceloLeite2604
 */
public class ChainAssembler {

  public <T extends ChainLink<T>> void assemble(Iterable<T> chainLinks) {

    T previous = null;
    for (T chainLink : chainLinks) {
      if (previous != null) {
        previous.setNext(chainLink);
      }
      previous = chainLink;
    }
  }

  public <X extends ChainLink<X>> void assemble(ChainContext<X> chainContext) {
    assemble(chainContext.getSortedChainLinks());
  }
}
