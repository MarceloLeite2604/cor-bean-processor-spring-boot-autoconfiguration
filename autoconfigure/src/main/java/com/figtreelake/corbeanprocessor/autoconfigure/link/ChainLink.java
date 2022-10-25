package com.figtreelake.corbeanprocessor.autoconfigure.link;

/**
 * This interface must be implemented by all classes that must be concatenated
 * together to create a chain of responsibility. Different types informed on
 * parameter "T" means different chains implementation and will be concatenated
 * separately.
 *
 * @param <T> An auto-reference type got the class/interface that implements/extends
 *            this interface.
 * @author MarceloLeite2604
 */
public interface ChainLink<T extends ChainLink<T>> {

  /**
   * Sets the next object on the chain of responsibility. This is used
   * automatically by COR bean processor library and should not be used for
   * any other purpose.
   *
   * @param next The next chain link object.
   */
  void setNext(T next);
}
